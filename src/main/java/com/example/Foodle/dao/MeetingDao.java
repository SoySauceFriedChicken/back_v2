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
import org.threeten.bp.Instant;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.example.Foodle.dto.request.meeting.MeetingDto;
import com.example.Foodle.dto.request.meetingPlace.MeetingPlaceDto;
import com.example.Foodle.dto.request.place.PlaceDto;
import com.example.Foodle.dto.request.user.UsersDto;
import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.entity.MeetingPlaceEntity;
import com.example.Foodle.entity.MeetingPlaceInfoEntity;
import com.example.Foodle.entity.PlaceEntity;
import com.example.Foodle.entity.UsersEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public List<PlaceDto> convertToMeetingDtos(List<Map<String, Object>> data) {
        ObjectMapper mapper = new ObjectMapper();
        List<PlaceDto> placeDtos = new ArrayList<>();

        for (Map<String, Object> map : data) {
            PlaceDto dto = mapper.convertValue(map, PlaceDto.class);
            placeDtos.add(dto);
        }
        return placeDtos;
    }

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

    public MeetingDto getMeetingsByMid(int mid) throws ExecutionException, InterruptedException {
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
            List<UsersDto> joiners = new ArrayList<>();

            for (String memberId : memberIds) {
                Query userRef = db.collection(USERS_COLLECTION_NAME).whereEqualTo("uid", memberId);
                ApiFuture<QuerySnapshot> userSnapshotFuture = userRef.get();
                QuerySnapshot userSnapshot = userSnapshotFuture.get();

                if (!userSnapshot.isEmpty()) {
                    UsersDto usersDto = userSnapshot.getDocuments().get(0).toObject(UsersDto.class);
                    joiners.add(usersDto);
                } else {
                    log.info("User with uid " + memberId + " not found");
                }
            }

            List<MeetingPlaceEntity> lists = meetEntity.getLists() != null ? meetEntity.getLists() : new ArrayList<>();
            List<MeetingPlaceDto> placeLists = new ArrayList<>();

            for (MeetingPlaceEntity list : lists) {
                Query placeRef = db.collection(PLACE_COLLECTION_NAME).whereEqualTo("placeName", list.getPlace().getPlaceName())
                        .whereEqualTo("latitude", list.getPlace().getLatitude())
                        .whereEqualTo("longtitude", list.getPlace().getLongtitude());
                ApiFuture<QuerySnapshot> placeSnapshotFuture = placeRef.get();
                QuerySnapshot placeSnapshot = placeSnapshotFuture.get();

                if (!placeSnapshot.isEmpty()) {
                    PlaceDto placeData = placeSnapshot.getDocuments().get(0).toObject(PlaceDto.class);
                    MeetingPlaceDto placeDto = new MeetingPlaceDto();
                    placeDto.setPlace(placeData);

                    String dateString = list.getTime();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
                    Date dates = null;

                    try {
                        dates = formatter.parse(dateString);
                        // System.out.println("Date: " + dates);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    placeDto.setTime(dates);

                    placeLists.add(placeDto);
                    // log.info("Place found: " + placeData);
                } else {
                    log.info("Place with placeName " + list.getPlace().getPlaceName() + " not found");
                }
            }

            String dateString = meetEntity.getDate();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
            Date dates = null;
            try {
                dates = formatter.parse(dateString);
                System.out.println("Date: " + dates);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            MeetingDto meetingDto = new MeetingDto(
                    meetEntity.getMid(),
                    meetEntity.getName(),
                    dates,
                    placeLists,
                    joiners
            );

            meetingDtos.add(meetingDto);
            // log.info("Meeting found: " + meetingDto);
        }
        meetingDtos.sort((m1, m2) -> m1.getMid() - m2.getMid());
        return meetingDtos;
    }

    public String saveMeet(MeetEntity meetEntity) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                                            .orderBy("mid", Query.Direction.DESCENDING)
                                            .limit(1)
                                            .get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        int size;
        if (!documents.isEmpty()) {
            QueryDocumentSnapshot document = documents.get(0);
            if(document.get("member") == null) {
                return "joiners is null";
            }
            size =  document.getLong("mid").intValue();
        } else {
            size =  0; // 컬렉션이 비어있다면 0을 반환
        }
        log.info("size: " + size);

        meetEntity.setMid(size + 1);
        // log.info("Saving meeting with mid " + meetEntity.getMid());
        db.collection(COLLECTION_NAME).document().set(meetEntity);
        return "Meeting created successfully!";

        // log.info("Meeting saved successfully!");
    }

    public String updateMeet(MeetEntity meet) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();

        // Null 체크
        if (meet == null) {
            throw new RuntimeException("Meeting data is null");
        }

        // MeetDto에서 mid 가져오기
        int mid = meet.getMid();
        List<Integer> members = new ArrayList<>();

        // MeetEntity로 변환
        MeetEntity newMeetEntity = new MeetEntity();
        newMeetEntity.setMid(meet.getMid());
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

        return "Meeting with mid " + mid + " updated successfully!";
    }

    public String addPlaceList(int mid, List<MeetingPlaceDto> meetplace) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference meetingsRef = db.collection(COLLECTION_NAME);
        Query query = meetingsRef.whereEqualTo("mid", mid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        if (!documents.isEmpty()) {
            DocumentSnapshot document = documents.get(0);
            MeetEntity meetEntity = document.toObject(MeetEntity.class);

            List<MeetingPlaceEntity> newLists = new ArrayList<>();

            for (MeetingPlaceDto placeDto : meetplace) {
                MeetingPlaceEntity newPlaceEntity = new MeetingPlaceEntity();
                MeetingPlaceInfoEntity placeInfoEntity = new MeetingPlaceInfoEntity(
                    placeDto.getPlace().getPlaceName(),
                    placeDto.getPlace().getLatitude(),
                    placeDto.getPlace().getLongtitude()
                );
                newPlaceEntity.setPlace(placeInfoEntity);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
                if (placeDto.getTime() instanceof Date) {
                    Date date = (Date) placeDto.getTime();
                    ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.of("UTC"));
                    newPlaceEntity.setTime(formatter.format(zonedDateTime));
                }
                
                // newPlaceEntity.setTime(placeDto.getTime().toString());
                newLists.add(newPlaceEntity);
            }

            meetEntity.setLists(newLists);
            document.getReference().set(meetEntity);
            return "Meeting created successfully!";
        } else {
            throw new RuntimeException("Document with mid " + mid + " not found");
        }
    }

//     public void updatePlaceList(int mid, List<Map<String, Object>> meetplace) throws InterruptedException, ExecutionException {
//         Firestore db = FirestoreClient.getFirestore();
//         CollectionReference meetingsRef = db.collection(COLLECTION_NAME);
//         Query query = meetingsRef.whereEqualTo("mid", mid);
//         ApiFuture<QuerySnapshot> querySnapshot = query.get();
//         List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

//         if (!documents.isEmpty()) {
//             DocumentSnapshot document = documents.get(0);
//             MeetEntity meetEntity = document.toObject(MeetEntity.class);

//             SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
//             List<Map<String, Object>> lists = meetEntity.getLists() != null ? meetEntity.getLists() : new ArrayList<>();
            
//             for (Map<String, Object> place : meetplace) {
//                 Object placeObject = place.get("place");
//                 if (placeObject instanceof Map) {
//                     Map<String, Object> placeMap = (Map<String, Object>) placeObject;
//                     String pid = (String) placeMap.get("pid");
//                     Object time = place.get("time");
    
//                     boolean updated = false;
//                     for (Map<String, Object> existingPlace : lists) {
//                         if (existingPlace.get("place").equals(pid)) {
//                             existingPlace.put("time", time.toString());
//                             updated = true;
//                             break;
//                         }
//                     }
    
//                     if (!updated) {
//                         Map<String, Object> newPlace = new HashMap<>();
//                         newPlace.put("place", pid);
//                         newPlace.put("time", time.toString());
//                         lists.add(newPlace);
//                     }
//                 } else {
//                     // Handle the error appropriately, e.g., log an error message or throw an exception
//                     throw new RuntimeException("Expected a Map<String, Object> but got: " + placeObject.getClass().getName());
//                 }
//             }
    
//             meetEntity.setLists(lists);
//             document.getReference().set(meetEntity);
//         } else {
//             throw new RuntimeException("Document with mid " + mid + " not found");
//         }
//     }

//     public void deletePlaceList(int mid, List<Map<String, Object>> meetplace) throws InterruptedException, ExecutionException {
//         Firestore db = FirestoreClient.getFirestore();
//         CollectionReference meetingsRef = db.collection(COLLECTION_NAME);
//         Query query = meetingsRef.whereEqualTo("mid", mid);
//         ApiFuture<QuerySnapshot> querySnapshot = query.get();
//         List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

//         if (!documents.isEmpty()) {
//             DocumentSnapshot document = documents.get(0);
//             MeetEntity meetEntity = document.toObject(MeetEntity.class);
    
//             List<Map<String, Object>> lists = meetEntity.getLists() != null ? meetEntity.getLists() : new ArrayList<>();
    
//             // Iterate over meetplace and remove corresponding places from lists
//             for (Map<String, Object> placeToRemove : meetplace) {
//                 Object placeObject = placeToRemove.get("place");
//                 if (placeObject instanceof Map) {
//                     Map<String, Object> placeMap = (Map<String, Object>) placeObject;
//                     String pidToRemove = (String) placeMap.get("pid");
    
//                     // Remove the place with the specified pid
//                     lists.removeIf(existingPlace -> {
//                         Object existingPlaceObject = existingPlace.get("place");
//                         if (existingPlaceObject instanceof String) {
//                             return existingPlaceObject.equals(pidToRemove);
//                         }
//                         return false;
//                     });
//                 } else {
//                     // Handle the error appropriately, e.g., log an error message or throw an exception
//                     throw new RuntimeException("Expected a Map<String, Object> but got: " + placeObject.getClass().getName());
//                 }
//             }
    
//             meetEntity.setLists(lists);
//             document.getReference().set(meetEntity);
//         } else {
//             throw new RuntimeException("Document with mid " + mid + " not found");
//         }
//     }

    public String addUserToMeeting(int mid, List<UsersEntity> joiners) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference meetingsRef = db.collection(COLLECTION_NAME);
        Query query = meetingsRef.whereEqualTo("mid", mid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        if (!documents.isEmpty()) {
            DocumentSnapshot document = documents.get(0);
            MeetEntity meetEntity = document.toObject(MeetEntity.class);
            
            List<String> newMembers = new ArrayList<>();
            for(UsersEntity joiner : joiners) {
                newMembers.add(joiner.getUid());
            }
            meetEntity.setMember(newMembers);
            document.getReference().set(meetEntity);
            return "Meeting created successfully!";
        } else {
            throw new RuntimeException("Document with mid " + mid + " not found");
        }
    }

    // 미팅 시간 업데이트
    public String updateTime(int mid, Date time) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference meetingsRef = db.collection(COLLECTION_NAME);
        Query query = meetingsRef.whereEqualTo("mid", mid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        if (!documents.isEmpty()) {
            DocumentSnapshot document = documents.get(0);
            MeetEntity meetEntity = document.toObject(MeetEntity.class);
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(time.toInstant(), ZoneId.of("UTC"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
            meetEntity.setDate(formatter.format(zonedDateTime));
            document.getReference().set(meetEntity);
            return "Meeting Time Updated successfully!";
        } else {
            throw new RuntimeException("Document with mid " + mid + " not found");
        }
    }

    public String deleteMeeting(MeetEntity meetEntity) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference meetingsRef = db.collection(COLLECTION_NAME);
        Query query = meetingsRef.whereEqualTo("mid", meetEntity.getMid());
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        if (!documents.isEmpty()) {
            DocumentSnapshot document = documents.get(0);
            document.getReference().delete();
            return "Meeting deleted successfully!";
        } else {
            throw new RuntimeException("Document with mid " + meetEntity.getMid() + " not found");
        }
    }
}