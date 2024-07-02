package com.example.Foodle.dao;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Repository;

import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.entity.PlaceEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class PlaceDao {
    public static final String COLLECTION_NAME = "Place";

    public List<PlaceEntity> getAllPlaces() throws ExecutionException, InterruptedException {
        List<PlaceEntity> list = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            list.add(document.toObject(PlaceEntity.class));
        }
        return list;
    }

    public List<PlaceEntity> getPlaceByPid(int pid) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).whereEqualTo("pid", pid).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<PlaceEntity> places = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            places.add(document.toObject(PlaceEntity.class));
        }
        return places;
    }


}
