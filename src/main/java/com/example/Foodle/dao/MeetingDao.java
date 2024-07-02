package com.example.Foodle.dao;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Repository;

import com.example.Foodle.entity.MeetEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class MeetingDao {
    public static final String COLLECTION_NAME = "Meet";

    public List<MeetEntity> getMeeting() throws ExecutionException, InterruptedException {
        List<MeetEntity> list = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            list.add(document.toObject(MeetEntity.class));
        }
        return list;
    }

    public List<MeetEntity> getMeetingsByUid(int uid) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).whereEqualTo("uid", uid).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<MeetEntity> meetings = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            meetings.add(document.toObject(MeetEntity.class));
        }
        return meetings;
    }

    public List<MeetEntity> getMeetingsByMid(int mid) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).whereEqualTo("mid", mid).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<MeetEntity> meetings = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            meetings.add(document.toObject(MeetEntity.class));
        }
        return meetings;
    }
}
