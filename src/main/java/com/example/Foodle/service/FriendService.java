package com.example.Foodle.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Foodle.dao.FriendDao;
import com.example.Foodle.entity.FriendEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.*;

@Service
public class FriendService {

    @Autowired
    private FriendDao friendDao;

    private static final String COLLECTION_NAME = "Friend";

    private Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }

    public List<FriendEntity> getFriendsByUid(int uid) throws ExecutionException, InterruptedException {
        Firestore db = getFirestore();
        CollectionReference friends = db.collection(COLLECTION_NAME);
        Query query = friends.whereEqualTo("uid", uid); // Use the correct Query class
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        
        List<FriendEntity> friendsByName = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            friendsByName.add(document.toObject(FriendEntity.class));
        }
        return friendsByName;
    }
}