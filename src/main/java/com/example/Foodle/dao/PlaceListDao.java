package com.example.Foodle.dao;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Repository;


import com.example.Foodle.entity.PlaceListEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class PlaceListDao {
    public static final String COLLECTION_NAME = "PlaceList";

    public List<PlaceListEntity> getUserPlaceLists(int uid) throws ExecutionException, InterruptedException {
        List<PlaceListEntity> list = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            list.add(document.toObject(PlaceListEntity.class));
        }
        return list;
    }

    public List<PlaceListEntity> getPlaceListByLid(int lid) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).whereEqualTo("lid", lid).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<PlaceListEntity> places = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            places.add(document.toObject(PlaceListEntity.class));
        }
        return places;
    }
}
