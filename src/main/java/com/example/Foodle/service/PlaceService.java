package com.example.Foodle.service;


import com.example.Foodle.dao.MeetingDao;
import com.example.Foodle.dao.PlaceDao;
import com.example.Foodle.dto.request.place.PlaceDto;
import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.entity.PlaceEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PlaceService {

    @Autowired
    private PlaceDao placeDao;

    private static final String COLLECTION_NAME = "Place";

    private Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }

    public List<PlaceDto> getAllPlaces() throws ExecutionException, InterruptedException {
        return placeDao.getAllPlaces();
    }
    public List<PlaceDto> getPlaceByPlaceName(String placeName) throws ExecutionException, InterruptedException {
        return placeDao.getPlaceByPlaceName(placeName);    
    }
    public List<PlaceDto> getPlaceByPlaceInfo(String placeName, Double latitude, Double longtitude) throws ExecutionException, InterruptedException {
        return placeDao.getPlaceByPlaceInfo(placeName, latitude, longtitude);    
    }

    public List<PlaceDto> getPlacesByCategory(String category) throws ExecutionException, InterruptedException {
        return placeDao.getPlacesByCategory(category);
    }

    public String fixRegularBreakMisplacement() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = db.collection("Place").get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
    
        for (QueryDocumentSnapshot document : documents) {
            PlaceDto place = document.toObject(PlaceDto.class);
            List<String> breaktime = place.getBreakTime();
            List<String> working = place.getWorking();
    
            boolean modified = false;
            List<String> updatedBreaktime = new ArrayList<>();
            List<String> updatedWorking = new ArrayList<>();
    
            for (int i = 0; i < breaktime.size(); i++) {
                String breaktimeSplit = breaktime.get(i);
                String workingSplit = working.get(i);
    
                if (breaktimeSplit.equals("정기휴무")) {
                    updatedBreaktime.add("");
                    updatedWorking.add("정기휴무");
                    modified = true;
                } else {
                    updatedBreaktime.add(breaktimeSplit);
                    updatedWorking.add(workingSplit);
                }
            }
    
            if (modified) {
                place.setBreakTime(updatedBreaktime);
                place.setWorking(updatedWorking);
                db.collection("Place").document(document.getId()).set(place);
            }
        }
    
        return "Regular break misplacement correction completed.";
    }
    
}
