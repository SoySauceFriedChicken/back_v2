package com.example.Foodle.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Repository;

import com.example.Foodle.entity.FriendEntity;
import com.example.Foodle.entity.MeetEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import lombok.extern.slf4j.Slf4j;


@Repository
@Slf4j
public class FriendDao {
    public static final String COLLECTION_NAME = "Friend";

    public List<FriendEntity> getFriendsByUid(int uid) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).whereEqualTo("uid", uid).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<FriendEntity> friends = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            friends.add(document.toObject(FriendEntity.class));
        }
        return friends;
    }
}
