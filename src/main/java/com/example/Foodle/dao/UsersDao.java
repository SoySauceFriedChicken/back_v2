package com.example.Foodle.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Repository;

import com.example.Foodle.dto.request.user.PreferredTimeDto;
import com.example.Foodle.dto.request.user.UsersDto;
import com.example.Foodle.entity.PreferredTimeEntity;
import com.example.Foodle.entity.UsersEntity;
import com.google.api.core.ApiFuture;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.*;
import com.google.cloud.storage.Acl.User;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class UsersDao {

    public static final String COLLECTION_NAME = "Users";

    public static LocalTime convertTimeStringToLocalTime(String timeString) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(timeString, timeFormatter);
    }

    public List<UsersDto> getUsers() throws ExecutionException, InterruptedException {
        List<UsersDto> list = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            UsersEntity user = document.toObject(UsersEntity.class);
            if(user.getPreferredTime() == null) {
                UsersDto userDto = new UsersDto(user.getUid(), user.getName(), user.getNickName(), user.getProfileImage(), null, user.getLikeWord(), user.getDislikeWord());
                list.add(userDto);
                continue;
            } else{
                UsersDto userDto = new UsersDto(user.getUid(), user.getName(), user.getNickName(), user.getProfileImage(), null, user.getLikeWord(), user.getDislikeWord());
                List<PreferredTimeDto> preferredTimeList = new ArrayList<>();
                PreferredTimeDto preferredTimeDto = new PreferredTimeDto();
                for(PreferredTimeEntity preferredTime : user.getPreferredTime()) {
                    preferredTimeDto.setDay(preferredTime.getDay());
                    preferredTimeDto.setStart(convertTimeStringToLocalTime(preferredTime.getStart()));
                    preferredTimeDto.setEnd(convertTimeStringToLocalTime(preferredTime.getEnd()));
                    preferredTimeList.add(preferredTimeDto);
                }
                userDto.setPreferredTime(preferredTimeList);
                list.add(userDto);
            }
            
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
            //log.info("User found: " + documents.get(0).toObject(UsersEntity.class));
            UsersEntity user = documents.get(0).toObject(UsersEntity.class);
            UsersDto userDto = new UsersDto(user.getUid(), user.getName(), user.getNickName(), user.getProfileImage(), null, user.getLikeWord(), user.getDislikeWord());
            List<PreferredTimeDto> preferredTimeList = new ArrayList<>();
            PreferredTimeDto preferredTimeDto = new PreferredTimeDto();
            if(user.getPreferredTime() == null) {
                return userDto;
            }
            for(PreferredTimeEntity preferredTime : user.getPreferredTime()) {
                preferredTimeDto.setDay(preferredTime.getDay());
                preferredTimeDto.setStart(convertTimeStringToLocalTime(preferredTime.getStart()));
                preferredTimeDto.setEnd(convertTimeStringToLocalTime(preferredTime.getEnd()));
                preferredTimeList.add(preferredTimeDto);
            }
            userDto.setPreferredTime(preferredTimeList);

            return userDto;
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
    
    public String saveUser(UsersEntity user) {
        Firestore db = FirestoreClient.getFirestore();
        if(db.collection(COLLECTION_NAME).whereEqualTo("uid", user.getUid()) != null) {
<<<<<<< HEAD
            db.collection(COLLECTION_NAME).document().set(user); // 자동 생성된 ID를 사용
            log.info("User saved successfully!");
            return "User saved successfully!";
        }
        log.info("User already exists!");
=======


            db.collection(COLLECTION_NAME).document().set(user); // 자동 생성된 ID를 사용
            // log.info("User saved successfully!");
            return "User saved successfully!";
        }
        // log.info("User already exists!");
>>>>>>> b4fbbc6 ([feat] String 리턴값 Json 형태로 수정 + 장소 검색시 Joiner의 장소 리스트)
        return "User already exists!";
    }
    
    public String updateUser(UsersDto usersDto) {
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
                ApiFuture<WriteResult> future = docRef.set(usersDto.toEntity(), SetOptions.merge());
                WriteResult result = future.get();
                log.info("Update time : " + result.getUpdateTime());
                return "User updated successfully!";
            } else {
                // Document does not exist, handle accordingly
                log.error("Document with uid {} not found.", usersDto.getUid());
                return "no user found";
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error updating user with uid " + usersDto.getUid(), e);
            return "error updating user";
        }
    }

    // private UsersEntity convertDtoToEntity(UsersDto usersDto) {
    //     return new UsersEntity(usersDto.getUid(), usersDto.getName(), usersDto.getNickName(), usersDto.getProfileImage(), usersDto.getPreferredTime(), usersDto.getLikeWord(), usersDto.getDislikeWord());
    // }

    public String deleteUser(String uid) {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference users = db.collection(COLLECTION_NAME);
        Query query = users.whereEqualTo("uid", uid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

            if (!documents.isEmpty()) {
                // Document exists, delete it
                DocumentReference docRef = documents.get(0).getReference();
                ApiFuture<WriteResult> future = docRef.delete();
                WriteResult result = future.get();
                log.info("Delete time : " + result.getUpdateTime());
                return "User deleted successfully!";
            } else {
                // Document does not exist, handle accordingly
                log.error("Document with uid {} not found.", uid);
                return "no user found";
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error deleting user with uid " + uid, e);
            return "error deleting user";
        }
    }

    public String deleteUser(String uid) {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference users = db.collection(COLLECTION_NAME);
        Query query = users.whereEqualTo("uid", uid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

            if (!documents.isEmpty()) {
                // Document exists, delete it
                DocumentReference docRef = documents.get(0).getReference();
                ApiFuture<WriteResult> future = docRef.delete();
                WriteResult result = future.get();
                log.info("Delete time : " + result.getUpdateTime());
                return "User deleted successfully!";
            } else {
                // Document does not exist, handle accordingly
                log.error("Document with uid {} not found.", uid);
                return "no user found";
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error deleting user with uid " + uid, e);
            return "error deleting user";
        }
    }
    
}