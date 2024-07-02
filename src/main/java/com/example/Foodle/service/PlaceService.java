package com.example.Foodle.service;


import com.example.Foodle.dao.MeetingDao;
import com.example.Foodle.dao.PlaceDao;
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

    public List<PlaceEntity> getAllPlaces() throws ExecutionException, InterruptedException {
        Firestore db = getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        
        List<PlaceEntity> places = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            places.add(document.toObject(PlaceEntity.class));
        }
        return places;
    }
    public List<PlaceEntity> getPlaceByPid(int uid) throws ExecutionException, InterruptedException {
        Firestore db = getFirestore();
        CollectionReference meetings = db.collection(COLLECTION_NAME);
        Query query = meetings.whereEqualTo("uid", uid); // Use the correct Query class
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        
        List<PlaceEntity> placeByPid = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            placeByPid.add(document.toObject(PlaceEntity.class));
        }
        return placeByPid;
    }

}
