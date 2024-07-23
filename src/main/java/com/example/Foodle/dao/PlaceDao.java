package com.example.Foodle.dao;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Repository;

import com.example.Foodle.dto.request.place.PlaceDto;
import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.entity.PlaceEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.apache.commons.text.similarity.LevenshteinDistance;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class PlaceDao {
    public static final String COLLECTION_NAME = "Place";

    public List<PlaceDto> getAllPlaces() throws ExecutionException, InterruptedException {
        List<PlaceDto> list = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            list.add(document.toObject(PlaceDto.class));
        }
        list.sort((place1, place2) -> place1.getPlaceName().compareTo(place2.getPlaceName()));
        return list;
    }

    private static final double SIMILARITY_THRESHOLD = 0.7; // 유사성 비율 임계값 (예: 70%)

    public List<PlaceDto> getPlaceByPlaceName(String placeName) throws ExecutionException, InterruptedException {
        if (placeName.isEmpty()) {
            return new ArrayList<PlaceDto>();
        }
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<PlaceDto> places = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            PlaceDto place = document.toObject(PlaceDto.class);
            if (isSimilar(place.getPlaceName(), placeName) || containsWord(place.getPlaceName(), placeName)) {
                places.add(place);
            }
        }
        return sortPlacesBySimilarity(places, placeName);
    }

    private boolean isSimilar(String source, String target) {
        int distance = getLevenshteinDistance(source, target);
        double similarity = 1.0 - ((double) distance / Math.max(source.length(), target.length()));
        return similarity >= SIMILARITY_THRESHOLD;
    }

    private int getLevenshteinDistance(String source, String target) {
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        return levenshteinDistance.apply(source, target);
    }

    private boolean containsWord(String source, String target) {
        return source.toLowerCase().contains(target.toLowerCase());
    }

    private List<PlaceDto> sortPlacesBySimilarity(List<PlaceDto> places, String target) {
        return places.stream()
                .sorted((place1, place2) -> {
                    double similarity1 = getSimilarity(place1.getPlaceName(), target);
                    double similarity2 = getSimilarity(place2.getPlaceName(), target);
                    return Double.compare(similarity2, similarity1); // 유사성이 높은 순으로 정렬
                })
                .collect(Collectors.toList());
    }

    private double getSimilarity(String source, String target) {
        int distance = getLevenshteinDistance(source, target);
        return 1.0 - ((double) distance / Math.max(source.length(), target.length()));
    }



    public List<PlaceDto> getPlaceByPlaceInfo(String placeName, Double latitude, Double longtitude) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).whereEqualTo("placeName", placeName).whereEqualTo("latitude", latitude).whereEqualTo("longtitude", longtitude).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<PlaceDto> places = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            places.add(document.toObject(PlaceDto.class));
        }
        return places;
    }

    public List<PlaceDto> getPlacesByCategory(String category) throws ExecutionException, InterruptedException {
        if(category == "") {
            return new ArrayList<PlaceDto>();
        }
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<PlaceDto> places = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            PlaceDto place = document.toObject(PlaceDto.class);
            if (containsWord(place.getCategory(), category)) {
                places.add(place);
            }
        }
        return places;
    }


}
