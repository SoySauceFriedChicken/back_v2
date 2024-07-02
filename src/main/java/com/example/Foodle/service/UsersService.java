package com.example.Foodle.service;


import com.example.Foodle.dao.UsersDao;
import com.example.Foodle.entity.UsersEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class UsersService {

    @Autowired
    private UsersDao usersDao;

    private static final String COLLECTION_NAME = "users";

    private Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }

    public List<UsersEntity> getAllUsers() throws ExecutionException, InterruptedException {
        return usersDao.getUsers();
    }

    public List<UsersEntity> findByUid(int uid) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference users = db.collection(COLLECTION_NAME);
        Query query = users.whereEqualTo("uid", uid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        List<UsersEntity> meetingsByName = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            meetingsByName.add(document.toObject(UsersEntity.class));
        }
        return null;
    }

}