package com.example.Foodle.dao;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.Foodle.dto.request.meeting.MeetingDto;
import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.entity.UsersEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class MeetingDao {
    private MeetingDto meetingDto;

    public static final String COLLECTION_NAME = "Meet";
    public static final String USERS_COLLECTION_NAME = "Users";
    public static final String PLACE_COLLECTION_NAME = "Place";

    public List<MeetingDto> getMeeting() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        ApiFuture<QuerySnapshot> meetingsFuture = db.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> meetingDocuments = meetingsFuture.get().getDocuments();

        log.info("Retrieving all meetings");
        return convertToMeetingDtos(db, meetingDocuments);
    }

    public List<MeetingDto> getMeetingsByUid(int uid) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).whereEqualTo("uid", uid).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        return convertToMeetingDtos(db, documents);
    }

    public List<MeetingDto> getMeetingsByMid(int mid) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).whereEqualTo("mid", mid).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        return convertToMeetingDtos(db, documents);
    }

    private List<MeetingDto> convertToMeetingDtos(Firestore db, List<QueryDocumentSnapshot> documents) throws ExecutionException, InterruptedException {
        List<MeetingDto> meetingDtos = new ArrayList<>();

        for (QueryDocumentSnapshot document : documents) {
            MeetEntity meetEntity = document.toObject(MeetEntity.class);

            List<Integer> memberIds = meetEntity.getMember();
            List<UsersEntity> joiners = new ArrayList<>();

            for (Integer memberId : memberIds) {
                Query userRef = db.collection(USERS_COLLECTION_NAME).whereEqualTo("uid", memberId);
                ApiFuture<QuerySnapshot> userSnapshotFuture = userRef.get();
                QuerySnapshot userSnapshot = userSnapshotFuture.get();

                if (!userSnapshot.isEmpty()) {
                    UsersEntity userEntity = userSnapshot.getDocuments().get(0).toObject(UsersEntity.class);
                    joiners.add(userEntity);
                    log.info("User found: " + userEntity);
                } else {
                    log.info("User with uid " + memberId + " not found");
                }
            }

            List<Map<String, Object>> lists = meetEntity.getLists() != null ? meetEntity.getLists() : new ArrayList<>();
            List<Map<String, Object>> placeLists = new ArrayList<>();

            for (Map<String, Object> list : lists) {
                Query placeRef = db.collection(PLACE_COLLECTION_NAME).whereEqualTo("pid", list.get("pid"));
                ApiFuture<QuerySnapshot> placeSnapshotFuture = placeRef.get();
                QuerySnapshot placeSnapshot = placeSnapshotFuture.get();

                if (!placeSnapshot.isEmpty()) {
                    Map<String, Object> placeData = placeSnapshot.getDocuments().get(0).getData();
                    // Create a new map to store the pid and place data
                    Map<String, Object> placeEntry = new HashMap<>();
                    placeEntry.put("place", placeData);
                    placeEntry.put("time", list.get("time"));

                    placeLists.add(placeEntry);
                    log.info("Place found: " + placeData);
                } else {
                    log.info("Place with pid " + list.get("pid") + " not found");
                }
            }

            MeetingDto meetingDto = new MeetingDto(
                meetEntity.getMid(),
                meetEntity.getUid(),
                meetEntity.getName(),
                meetEntity.getDate(),
                placeLists,
                joiners
            );

            meetingDtos.add(meetingDto);
            log.info("Meeting found: " + meetingDto);
        }

        return meetingDtos;
    }

    public void saveMeet(MeetEntity meetEntity) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection(COLLECTION_NAME).document().set(meetEntity);


    }

    public void updateMeet(MeetEntity meetEntity) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();

        // Null 체크
        if (meetEntity == null) {
            throw new IllegalArgumentException("MeetingDto is null");
        }

        // MeetDto에서 mid 가져오기
        int mid = meetEntity.getMid();

        // MeetEntity로 변환
        MeetEntity newMeetEntity = new MeetEntity();
        newMeetEntity.setMid(meetEntity.getMid());
        newMeetEntity.setUid(meetEntity.getUid());
        newMeetEntity.setName(meetEntity.getName());
        newMeetEntity.setDate(meetEntity.getDate());
        newMeetEntity.setMember(meetEntity.getMember());
        newMeetEntity.setLists(meetEntity.getLists());
        // lists와 joiners는 필요에 따라 meetEntity에 맞게 설정

        log.info("Updating meeting with mid " + mid);
        log.info("New meeting data: " + newMeetEntity);

        // Firestore에서 업데이트할 문서 가져오기
        CollectionReference meetingsRef = db.collection(COLLECTION_NAME);
        ApiFuture<QuerySnapshot> query = meetingsRef.whereEqualTo("mid", mid).get();
        QuerySnapshot querySnapshot = query.get();
        
        // 검색된 문서가 있다면 업데이트 수행
        if (!querySnapshot.isEmpty()) {
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                ApiFuture<WriteResult> updateFuture = document.getReference().set(meetEntity);
                // 업데이트가 완료될 때까지 대기
                updateFuture.get();
            }
        } else {
            throw new RuntimeException("Document with mid " + mid + " not found");
        }

        System.out.println("Meeting with mid " + mid + " updated successfully!");
    }
}