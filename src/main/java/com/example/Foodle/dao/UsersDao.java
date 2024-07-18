package com.example.Foodle.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Repository;

import com.example.Foodle.dto.request.user.UsersDto;
import com.example.Foodle.entity.UsersEntity;
import com.google.api.core.ApiFuture;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.*;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class UsersDao {

    public static final String COLLECTION_NAME = "Users";

    public List<UsersDto> getUsers() throws ExecutionException, InterruptedException {
        List<UsersDto> list = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            list.add(document.toObject(UsersDto.class));
        }
        return list;
    }

    public UsersDto findByUid(String uid) throws InterruptedException, ExecutionException {
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference users = firestore.collection(COLLECTION_NAME);
        Query query = users.whereEqualTo("uid", uid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
    
        if (!documents.isEmpty()) {
            log.info("User found: " + documents.get(0).toObject(UsersDto.class));
            return documents.get(0).toObject(UsersDto.class);
        } else {
            log.info("User not found");
            return null; // 또는 예외를 던지거나 원하는 로직을 추가
        }
    }

    public UsersEntity getUsersEntity(String uid) throws InterruptedException, ExecutionException {
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference users = firestore.collection(COLLECTION_NAME);
        Query query = users.whereEqualTo("uid", uid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
    
        if (!documents.isEmpty()) {
            log.info("User found: " + documents.get(0).toObject(UsersEntity.class));
            return documents.get(0).toObject(UsersEntity.class);
        } else {
            log.info("User not found");
            return null; // 또는 예외를 던지거나 원하는 로직을 추가
        }
    }
    
    public void saveUser(UsersEntity user) {
        Firestore db = FirestoreClient.getFirestore();
        if(db.collection(COLLECTION_NAME).whereEqualTo("uid", user.getUid()) != null) {
            log.info("User already exists!");
            return;
        }
        db.collection(COLLECTION_NAME).document().set(user); // 자동 생성된 ID를 사용
        log.info("User saved successfully!");
    }
    
    public void updateUser(UsersDto usersDto) {
        Firestore db = FirestoreClient.getFirestore();

        // Find document based on uid
        CollectionReference users = db.collection(COLLECTION_NAME);
        Query query = users.whereEqualTo("uid", usersDto.getUid());
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

            if (!documents.isEmpty()) {
                // Document exists, update it
                DocumentReference docRef = documents.get(0).getReference();
                ApiFuture<WriteResult> future = docRef.set(convertDtoToEntity(usersDto), SetOptions.merge());
                WriteResult result = future.get();
                log.info("Update time : " + result.getUpdateTime());
            } else {
                // Document does not exist, handle accordingly
                log.error("Document with uid {} not found.", usersDto.getUid());
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error updating user with uid " + usersDto.getUid(), e);
        }
    }

    private UsersEntity convertDtoToEntity(UsersDto usersDto) {
        return new UsersEntity(usersDto.getUid(), usersDto.getName(), usersDto.getNickName(), usersDto.getProfileImage());
    }
    
}