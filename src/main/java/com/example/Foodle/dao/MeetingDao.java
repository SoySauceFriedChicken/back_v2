package com.example.Foodle.dao;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.Foodle.dto.request.meeting.MeetingDto;
import com.example.Foodle.dto.request.place.PlaceDto;
import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.entity.PlaceEntity;
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
import com.google.protobuf.ByteString;

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

    public List<MeetingDto> getMeetingsByUid(String uid) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).whereArrayContains("member", uid).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        return convertToMeetingDtos(db, documents);
    }

    public MeetingDto getMeetingsByMid(String mid) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference meetingsRef = firestore.collection(COLLECTION_NAME);
        ApiFuture<QuerySnapshot> future = meetingsRef.whereEqualTo("mid", mid).get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<MeetingDto> meetingDtos = convertToMeetingDtos(firestore, documents);
        return meetingDtos.isEmpty() ? null : meetingDtos.get(0);
    }

    private List<MeetingDto> convertToMeetingDtos(Firestore db, List<QueryDocumentSnapshot> documents) throws ExecutionException, InterruptedException {
        List<MeetingDto> meetingDtos = new ArrayList<>();

        for (QueryDocumentSnapshot document : documents) {
            MeetEntity meetEntity = document.toObject(MeetEntity.class);

            List<String> memberIds = meetEntity.getMember();
            List<UsersEntity> joiners = new ArrayList<>();

            for (String memberId : memberIds) {
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
                Query placeRef = db.collection(PLACE_COLLECTION_NAME).whereEqualTo("pid", list.get("place"));
                ApiFuture<QuerySnapshot> placeSnapshotFuture = placeRef.get();
                QuerySnapshot placeSnapshot = placeSnapshotFuture.get();


                if (!placeSnapshot.isEmpty()) {
                    Map<String, Object> placeData = placeSnapshot.getDocuments().get(0).getData();
                    // Create a new map to store the pid and place data
                    Map<String, Object> placeEntry = new HashMap<>();
                    placeEntry.put("place", placeData);

                    log.info("Place data: " + list.get("time"));
                    String dateString = list.get("time").toString();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
                    Date dates = null;

                    try {
                        dates = formatter.parse(dateString);
                        System.out.println("Date: " + dates);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    placeEntry.put("time", dates);

                    placeLists.add(placeEntry);
                    log.info("Place found: " + placeData);
                } else {
                    log.info("Place with pid " + list.get("pid") + " not found");
                }
            }

            String dateString = meetEntity.getDate();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss +dddd");
            Date dates = null;

            try {
                dates = formatter.parse(dateString);
                System.out.println("Date: " + dates);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            MeetingDto meetingDto = new MeetingDto(
                meetEntity.getMid(),
                meetEntity.getUid(),
                meetEntity.getName(),
                dates,  // try-catch 블록 외부에서 선언된 dates 변수 사용
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

        log.info("Meeting saved successfully!");
    }

    public void updateMeet(MeetEntity meet) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();

        // Null 체크
        if (meet == null) {
            throw new IllegalArgumentException("MeetingDto is null");
        }

        // MeetDto에서 mid 가져오기
        String mid = meet.getMid();
        List<Integer> members = new ArrayList<>();

        // MeetEntity로 변환
        MeetEntity newMeetEntity = new MeetEntity();
        newMeetEntity.setMid(meet.getMid());
        newMeetEntity.setUid(meet.getUid());
        newMeetEntity.setName(meet.getName());
        newMeetEntity.setDate(meet.getDate());
        newMeetEntity.setMember(meet.getMember());
        newMeetEntity.setLists(meet.getLists());
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
                ApiFuture<WriteResult> updateFuture = document.getReference().set(newMeetEntity);
                // 업데이트가 완료될 때까지 대기
                updateFuture.get();
            }
        } else {
            throw new RuntimeException("Document with mid " + mid + " not found");
        }

        System.out.println("Meeting with mid " + mid + " updated successfully!");
    }

    public void addPlaceList(String mid, List<Map<String, Object>> meetplace) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference meetingsRef = db.collection(COLLECTION_NAME);
        Query query = meetingsRef.whereEqualTo("mid", mid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        if (!documents.isEmpty()) {
            DocumentSnapshot document = documents.get(0);
            MeetEntity meetEntity = document.toObject(MeetEntity.class);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
            List<Map<String, Object>> lists = meetEntity.getLists() != null ? meetEntity.getLists() : new ArrayList<>();
            
            Map<String, Object> newPlace = new HashMap<>();
            for(Map<String, Object> place : meetplace){
                Object placeObject = place.get("place");
                if (placeObject instanceof Map) {
                    Map<String, Object> placeMap = (Map<String, Object>) placeObject;
                    String pid = (String) placeMap.get("pid");
                    Object time = place.get("time");

                    newPlace.put("place", pid);
                    newPlace.put("time", time.toString());

                    lists.add(newPlace);
                } else {
                    // Handle the error appropriately, e.g., log an error message or throw an exception
                    throw new RuntimeException("Expected a Map<String, Object> but got: " + placeObject.getClass().getName());
                }
            }
            
            meetEntity.setLists(lists);
            document.getReference().set(meetEntity);
        } else {
            throw new RuntimeException("Document with mid " + mid + " not found");
        }
    }

    public void updatePlaceList(String mid, List<Map<String, Object>> meetplace) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference meetingsRef = db.collection(COLLECTION_NAME);
        Query query = meetingsRef.whereEqualTo("mid", mid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        if (!documents.isEmpty()) {
            DocumentSnapshot document = documents.get(0);
            MeetEntity meetEntity = document.toObject(MeetEntity.class);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
            List<Map<String, Object>> lists = meetEntity.getLists() != null ? meetEntity.getLists() : new ArrayList<>();
            
            for (Map<String, Object> place : meetplace) {
                Object placeObject = place.get("place");
                if (placeObject instanceof Map) {
                    Map<String, Object> placeMap = (Map<String, Object>) placeObject;
                    String pid = (String) placeMap.get("pid");
                    Object time = place.get("time");
    
                    boolean updated = false;
                    for (Map<String, Object> existingPlace : lists) {
                        if (existingPlace.get("place").equals(pid)) {
                            existingPlace.put("time", time.toString());
                            updated = true;
                            break;
                        }
                    }
    
                    if (!updated) {
                        Map<String, Object> newPlace = new HashMap<>();
                        newPlace.put("place", pid);
                        newPlace.put("time", time.toString());
                        lists.add(newPlace);
                    }
                } else {
                    // Handle the error appropriately, e.g., log an error message or throw an exception
                    throw new RuntimeException("Expected a Map<String, Object> but got: " + placeObject.getClass().getName());
                }
            }
    
            meetEntity.setLists(lists);
            document.getReference().set(meetEntity);
        } else {
            throw new RuntimeException("Document with mid " + mid + " not found");
        }
    }

    public void deletePlaceList(String mid, List<Map<String, Object>> meetplace) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference meetingsRef = db.collection(COLLECTION_NAME);
        Query query = meetingsRef.whereEqualTo("mid", mid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        if (!documents.isEmpty()) {
            DocumentSnapshot document = documents.get(0);
            MeetEntity meetEntity = document.toObject(MeetEntity.class);
    
            List<Map<String, Object>> lists = meetEntity.getLists() != null ? meetEntity.getLists() : new ArrayList<>();
    
            // Iterate over meetplace and remove corresponding places from lists
            for (Map<String, Object> placeToRemove : meetplace) {
                Object placeObject = placeToRemove.get("place");
                if (placeObject instanceof Map) {
                    Map<String, Object> placeMap = (Map<String, Object>) placeObject;
                    String pidToRemove = (String) placeMap.get("pid");
    
                    // Remove the place with the specified pid
                    lists.removeIf(existingPlace -> {
                        Object existingPlaceObject = existingPlace.get("place");
                        if (existingPlaceObject instanceof String) {
                            return existingPlaceObject.equals(pidToRemove);
                        }
                        return false;
                    });
                } else {
                    // Handle the error appropriately, e.g., log an error message or throw an exception
                    throw new RuntimeException("Expected a Map<String, Object> but got: " + placeObject.getClass().getName());
                }
            }
    
            meetEntity.setLists(lists);
            document.getReference().set(meetEntity);
        } else {
            throw new RuntimeException("Document with mid " + mid + " not found");
        }
    }
}