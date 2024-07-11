package com.example.Foodle.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Repository;

import com.example.Foodle.entity.UsersEntity;
import com.google.api.core.ApiFuture;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.*;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class UsersDao {

    public static final String COLLECTION_NAME = "Users";

    public List<UsersEntity> getUsers() throws ExecutionException, InterruptedException {
        List<UsersEntity> list = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            list.add(document.toObject(UsersEntity.class));
        }
        return list;
    }

    public UsersEntity findByUid(String uid) throws InterruptedException, ExecutionException {
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
        db.collection(COLLECTION_NAME).document().set(user); // 자동 생성된 ID를 사용
        log.info("User saved successfully!");
    }
    
    public void updateUser(UsersEntity user) {
        Firestore db = FirestoreClient.getFirestore();
    
        // uid를 기반으로 문서를 찾기
        CollectionReference users = db.collection(COLLECTION_NAME);
        Query query = users.whereEqualTo("uid", user.getUid());
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
    
        try {
            List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
    
            if (!documents.isEmpty()) {
                // 문서가 존재하는 경우 업데이트
                DocumentReference docRef = documents.get(0).getReference();
                ApiFuture<WriteResult> future = docRef.set(user, SetOptions.merge());
                WriteResult result = future.get();
                // System.out.println("Update time : " + result.getUpdateTime());
                log.info("Update time : " + result.getUpdateTime());
            } else {
                // 문서가 존재하지 않는 경우 새로 생성 (필요 시)
                DocumentReference docRef = db.collection(COLLECTION_NAME).document();
                ApiFuture<WriteResult> future = docRef.set(user);
                WriteResult result = future.get();
                // System.out.println("New document created. Update time : " + result.getUpdateTime());
                log.info("New document created. Update time : " + result.getUpdateTime());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            // 예외 처리를 추가로 하고 싶다면 여기에 작성
        }
    }
}