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

    private static final int MAX_DISTANCE = 3; // Levenshtein 거리 임계값 설정

    public List<PlaceDto> getPlaceByPlaceName(String placeName) throws ExecutionException, InterruptedException {
        if(placeName == "") {
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
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        int distance = levenshteinDistance.apply(source, target);
        return distance <= MAX_DISTANCE;
    }

    private boolean containsWord(String source, String target) {
        return source.toLowerCase().contains(target.toLowerCase());
    }

    private List<PlaceDto> sortPlacesBySimilarity(List<PlaceDto> places, String target) {
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        return places.stream()
                .sorted((place1, place2) -> {
                    int distance1 = levenshteinDistance.apply(place1.getPlaceName(), target);
                    int distance2 = levenshteinDistance.apply(place2.getPlaceName(), target);
                    return Integer.compare(distance1, distance2);
                })
                .collect(Collectors.toList());
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
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).whereEqualTo("category", category).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<PlaceDto> places = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            places.add(document.toObject(PlaceDto.class));
        }
        return places;
    }


}
