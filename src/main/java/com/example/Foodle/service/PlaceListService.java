package com.example.Foodle.service;


import com.example.Foodle.dao.MeetingDao;
import com.example.Foodle.dao.PlaceDao;
import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.entity.PlaceEntity;
import com.example.Foodle.entity.PlaceListEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class PlaceListService {
    @Autowired
    private PlaceDao placeDao;

    private static final String COLLECTION_NAME = "PlaceList";

    private Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }

    public List<PlaceListEntity> getUserPlaceLists(int uid) throws ExecutionException, InterruptedException {
        Firestore db = getFirestore();
        CollectionReference placeLists = db.collection(COLLECTION_NAME);
        Query query = placeLists.whereEqualTo("uid", uid); // Use the correct Query class
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        
        List<PlaceListEntity> userplaceLists = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            userplaceLists.add(document.toObject(PlaceListEntity.class));
        }
        return userplaceLists;
    }
    public List<PlaceListEntity> getPlaceListByLid(int lid) throws ExecutionException, InterruptedException {
        Firestore db = getFirestore();
        CollectionReference placeLists = db.collection(COLLECTION_NAME);
        Query query = placeLists.whereEqualTo("lid", lid); // Use the correct Query class
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        
        List<PlaceListEntity> placeListInfo = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            placeListInfo.add(document.toObject(PlaceListEntity.class));
        }
        return placeListInfo;
    }
}
