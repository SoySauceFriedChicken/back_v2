package com.example.Foodle.service;


import com.example.Foodle.dao.UsersDao;
import com.example.Foodle.entity.UsersEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.lang.System.in;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
public class UsersService {

    @Autowired
    private UsersDao usersDao;

    private static final String COLLECTION_NAME = "Users";

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

    public void saveUser(UsersEntity user) {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference newDocRef = db.collection(COLLECTION_NAME).document(); // 자동 생성된 ID를 사용
        ApiFuture<WriteResult> collectionsApiFuture = newDocRef.set(user);

        try {
            WriteResult result = collectionsApiFuture.get();
            // System.out.println("Update time : " + result.getUpdateTime());
            log.info("Update time : " + result.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            // 예외 처리를 추가로 하고 싶다면 여기에 작성
        }
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