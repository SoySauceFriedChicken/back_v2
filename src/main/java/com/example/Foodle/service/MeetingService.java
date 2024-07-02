package com.example.Foodle.service;

import com.example.Foodle.dao.MeetingDao;
import com.example.Foodle.entity.MeetEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MeetingService {

    @Autowired
    private MeetingDao meetingDao;

    private static final String COLLECTION_NAME = "meetings";

    private Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }

    public List<MeetEntity> getAllMeetings() throws ExecutionException, InterruptedException {
        Firestore db = getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        
        List<MeetEntity> meetings = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            meetings.add(document.toObject(MeetEntity.class));
        }
        return meetings;
    }
    public List<MeetEntity> getMeetingsByUid(int uid) throws ExecutionException, InterruptedException {
        Firestore db = getFirestore();
        CollectionReference meetings = db.collection(COLLECTION_NAME);
        Query query = meetings.whereEqualTo("uid", uid); // Use the correct Query class
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        
        List<MeetEntity> meetingsByName = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            meetingsByName.add(document.toObject(MeetEntity.class));
        }
        return meetingsByName;
    }

    public List<MeetEntity> getMeetingsByMid(int mid) throws ExecutionException, InterruptedException{
        Firestore db = getFirestore();
        CollectionReference meetings = db.collection(COLLECTION_NAME);
        Query query = meetings.whereEqualTo("mid", mid); // Use the correct Query class
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        
        List<MeetEntity> meetingsByName = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            meetingsByName.add(document.toObject(MeetEntity.class));
        }
        return meetingsByName;
    }

}
