package com.example.Foodle.dao;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.threeten.bp.Instant;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.example.Foodle.dto.request.meeting.MeetingDto;
import com.example.Foodle.dto.request.meetingPlace.MeetingPlaceDto;
import com.example.Foodle.dto.request.place.PlaceDto;
import com.example.Foodle.dto.request.placeList.PlaceListDto;
import com.example.Foodle.dto.request.user.PreferredTimeDto;
import com.example.Foodle.dto.request.user.UsersDto;
import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.entity.MeetingPlaceEntity;
import com.example.Foodle.entity.MeetingPlaceInfoEntity;
import com.example.Foodle.entity.PlaceEntity;
import com.example.Foodle.entity.PreferredTimeEntity;
import com.example.Foodle.entity.UsersEntity;
import com.example.Foodle.service.AdminService;
import com.example.Foodle.service.FirestoreService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.Transaction;
import com.google.cloud.firestore.Transaction.Function;
import com.google.protobuf.ByteString;

import io.jsonwebtoken.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class MeetingDao {
    @Autowired
    private AdminService adminService;
    private MeetingDto meetingDto;

    // private Firestore firestore = FirestoreClient.getFirestore();

    private static final double SIMILARITY_THRESHOLD = 0.7; // 유사성 비율 임계값 (예: 70%)
    
    public static final String COLLECTION_NAME = "Meet";
    public static final String USERS_COLLECTION_NAME = "Users";
    public static final String PLACE_COLLECTION_NAME = "Place";
    private static final String MEETINGS_COLLECTION_NAME = "Meet";

    public static LocalTime convertTimeStringToLocalTime(String time) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(time, timeFormatter);
    }

    public List<PlaceDto> convertToMeetingDtos(List<Map<String, Object>> data) {
        ObjectMapper mapper = new ObjectMapper();
        List<PlaceDto> placeDtos = new ArrayList<>();

        for (Map<String, Object> map : data) {
            PlaceDto dto = mapper.convertValue(map, PlaceDto.class);
            placeDtos.add(dto);
        }
        return placeDtos;
    }

    // /**
    //  * 업데이트를 트랜잭션으로 처리하는 메서드
    //  * 
    //  * @param mid 업데이트할 미팅의 ID
    //  * @param updates 업데이트할 데이터
    //  * @throws Exception 예외 처리
    //  */
    // public void updateMeetingTransaction(int meetingId, Map<String, Object> updates) throws Exception {
    //     ApiFuture<Void> transactionFuture = firestore.runTransaction(new Transaction.Function<Void>() {
    //         @Override
    //         public Void updateCallback(Transaction transaction) throws Exception {
    //             CollectionReference meetingsRef = firestore.collection(COLLECTION_NAME);
    //             Query query = meetingsRef.whereEqualTo("mid", meetingId);
    //             QuerySnapshot querySnapshot = transaction.get(query).get();

    //             if (!querySnapshot.isEmpty()) {
    //                 DocumentSnapshot document = querySnapshot.getDocuments().get(0);
    //                 DocumentReference docRef = document.getReference();
    //                 transaction.update(docRef, updates);
    //             } else {
    //                 throw new RuntimeException("Document with mid " + meetingId + " not found");
    //             }
    //             return null;
    //         }
    //     });

    //     try {
    //         transactionFuture.get();
    //     } catch (InterruptedException | ExecutionException e) {
    //         throw new RuntimeException("Transaction failed: " + e.getMessage());
    //     }
    // }

    public List<MeetingDto> getMeeting() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> meetingsFuture = db.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> meetingDocuments = meetingsFuture.get().getDocuments();

        return convertToMeetingDtos(db, meetingDocuments);
        // log.info("Retrieving all meetings");
        // List<MeetingDto> meetingDtos = convertToMeetingDtos(db, meetingDocuments);
        // List<MeetingDto> newMeetingDtos = new ArrayList<>();
        // MeetingDto newMeetingDto = new MeetingDto();
       
        // for(MeetingDto meetingDto : meetingDtos) {
        //     newMeetingDto = new MeetingDto(meetingDto.getMid(), meetingDto.getName(), meetingDto.getDate(), meetingDto.getPlaces(), null);
        //     List<UsersDto> list = new ArrayList<>();
        //     for(UsersDto user : meetingDto.getJoiners()) {
        //         UsersDto userDto = new UsersDto(user.getUid(), user.getName(), user.getNickName(), user.getProfileImage(), null, user.getLikeWord(), user.getDislikeWord());
        //         List<PreferredTimeDto> preferredTimeList = new ArrayList<>();
        //         PreferredTimeDto preferredTimeDto = new PreferredTimeDto();
        //         for(PreferredTimeDto time : user.getPreferredTime()) {
        //             preferredTimeDto.setDay(time.getDay());
        //             preferredTimeDto.setStart(convertTimeStringToLocalTime(time.getStart()));
        //             preferredTimeDto.setEnd(convertTimeStringToLocalTime(time.getEnd()));
        //             preferredTimeList.add(preferredTimeDto);
        //         }
        //         userDto.setPreferredTime(preferredTimeList);
        //         list.add(userDto);
        //     }
        //     meetingDto.getJoiners().clear();
        //     meetingDto.setJoiners(list);
        // }
        // newMeetingDtos.add(newMeetingDto);
        // return meetingDtos;
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
                    UsersEntity usersEntity = userSnapshot.getDocuments().get(0).toObject(UsersEntity.class);                    
                    UsersDto userDto = new UsersDto(usersEntity.getUid(), usersEntity.getName(), usersEntity.getNickName(), usersEntity.getProfileImage(), null, usersEntity.getLikeWord(), usersEntity.getDislikeWord());
                    List<PreferredTimeDto> preferredTimeList = new ArrayList<>();
                    PreferredTimeDto preferredTimeDto = new PreferredTimeDto();
                    if(usersEntity.getPreferredTime() != null) {
                        for(PreferredTimeEntity time : usersEntity.getPreferredTime()) {
                            preferredTimeDto.setDay(time.getDay());
                            preferredTimeDto.setStart(convertTimeStringToLocalTime(time.getStart()));
                            preferredTimeDto.setEnd(convertTimeStringToLocalTime(time.getEnd()));                            preferredTimeList.add(preferredTimeDto);
                        }
                        userDto.setPreferredTime(preferredTimeList);
                        joiners.add(userDto);   
                    }
                    else {
                        // userDto.setPreferredTime(null);
                        joiners.add(userDto);
                    }

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


    /*
    public String updateMeet(MeetEntity meet) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();

        ApiFuture<Void> transactionFuture = db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void updateCallback(Transaction transaction) throws Exception {
                int mid = meet.getMid();
                CollectionReference meetingsRef = db.collection(COLLECTION_NAME);
                Query query = meetingsRef.whereEqualTo("mid", mid);
                QuerySnapshot querySnapshot = transaction.get(query).get();

                if (!querySnapshot.isEmpty()) {
                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                    DocumentReference docRef = document.getReference();
                    transaction.set(docRef, meet);
                } else {
                    throw new RuntimeException("Document with mid " + mid + " not found");
                }
                return null;
            }
        });

        try {
            transactionFuture.get();
            return "Meeting with mid " + meet.getMid() + " updated successfully!";
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Transaction failed: " + e.getMessage());
        }
    }

     */

    public String updateMeet(MeetEntity meet) throws InterruptedException, ExecutionException, java.io.IOException {
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

                FirestoreService firestoreService = new FirestoreService();

                // 예시로 targetToken, title, body, id, isEnd를 설정
                String targetToken = firestoreService.getFcmToken(meet.getMember().get(1)); // 실제 토큰으로 교체해야 함
                String title = "Meeting Updated";
                String body = "Meeting with ID " + mid + " has been updated";
                String id = String.valueOf(mid);
                String isEnd = "false";

                try {
                    adminService.sendPushMessage(targetToken, title, body, id, isEnd);
                } catch (IOException | JsonProcessingException e) {
                    log.error("Failed to send push message", e);
                }
            }
        } else {
            throw new RuntimeException("Document with mid " + mid + " not found");
        }

        return "Meeting with mid " + mid + " updated successfully!";
    }

    public String addPlaceList(int mid, List<MeetingPlaceDto> meetplace) throws InterruptedException, ExecutionException, java.io.IOException {
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

            FirestoreService firestoreService = new FirestoreService();

            // 예시로 targetToken, title, body, id, isEnd를 설정
            String targetToken = firestoreService.getFcmToken(meetEntity.getMember().get(1)); // 실제 토큰으로 교체해야 함
            String title = "Meeting Updated";
            String body = "Meeting with ID " + mid + " has been updated - new place added";
            String id = String.valueOf(mid);
            String isEnd = "false";

            try {
                adminService.sendPushMessage(targetToken, title, body, id, isEnd);
            } catch (IOException | JsonProcessingException e) {
                log.error("Failed to send push message", e);
            }
            
            return "Meeting places Updated successfully!";
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

    public String addUserToMeeting(int mid, List<UsersDto> joiners) throws InterruptedException, ExecutionException, java.io.IOException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference meetingsRef = db.collection(COLLECTION_NAME);
        Query query = meetingsRef.whereEqualTo("mid", mid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        if (!documents.isEmpty()) {
            DocumentSnapshot document = documents.get(0);
            MeetEntity meetEntity = document.toObject(MeetEntity.class);
            
            List<String> newMembers = new ArrayList<>();
            for(UsersDto joiner : joiners) {
                newMembers.add(joiner.getUid());
            }
            meetEntity.setMember(newMembers);
            document.getReference().set(meetEntity);

            FirestoreService firestoreService = new FirestoreService();

            // // 예시로 targetToken, title, body, id, isEnd를 설정
            // String targetToken = firestoreService.getFcmToken(joiners.get(1).getUid()); // 실제 토큰으로 교체해야 함
            // String title = "Meeting Updated";
            // String body = "Meeting with ID " + mid + " has been updated - new joiners added";
            // String id = String.valueOf(mid);
            // String isEnd = "false";

            // try {
            //     adminService.sendPushMessage(targetToken, title, body, id, isEnd);
            // } catch (IOException | JsonProcessingException e) {
            //     log.error("Failed to send push message", e);
            // }
            return "Joiner Updated successfully!";
        } else {
            throw new RuntimeException("Document with mid " + mid + " not found");
        }
    }

    // 유저 삭제
    public String deleteUserFromMeeting(int mid, UsersDto joiner) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference meetingsRef = db.collection(COLLECTION_NAME);
        Query query = meetingsRef.whereEqualTo("mid", mid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        if (!documents.isEmpty()) {
            DocumentSnapshot document = documents.get(0);
            MeetEntity meetEntity = document.toObject(MeetEntity.class);
            List<String> members = meetEntity.getMember();
            members.remove(joiner.getUid());
            meetEntity.setMember(members);
            document.getReference().set(meetEntity);
            return "Delete Joiner successfully!";
        } else {
            throw new RuntimeException("Document with mid " + mid + " not found");
        }
    }

    // 미팅 시간 업데이트
    public String updateTime(int mid, Date time) throws InterruptedException, ExecutionException, java.io.IOException {
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

            FirestoreService firestoreService = new FirestoreService();

            // // 예시로 targetToken, title, body, id, isEnd를 설정
            // String targetToken = firestoreService.getFcmToken(meetEntity.getMember().get(1)); // 실제 토큰으로 교체해야 함
            // String title = "Meeting Updated";
            // String body = "Meeting with ID " + mid + " has been updated - time changed";
            // String id = String.valueOf(mid);
            // String isEnd = "false";

            // try {
            //     adminService.sendPushMessage(targetToken, title, body, id, isEnd);
            // } catch (IOException | JsonProcessingException e) {
            //     log.error("Failed to send push message", e);
            // }
            
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

    // public List<PlaceDto> getPreferredPlaceByPlaceName(int mid, String placeName) throws InterruptedException, ExecutionException {
    //     Firestore db = FirestoreClient.getFirestore();
    //     CollectionReference meetingsRef = db.collection(COLLECTION_NAME);
    //     Query query = meetingsRef.whereEqualTo("mid", mid);
    //     ApiFuture<QuerySnapshot> querySnapshot = query.get();
    //     List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

    //     if (!documents.isEmpty()) {
    //         DocumentSnapshot document = documents.get(0);
    //         MeetEntity meetEntity = document.toObject(MeetEntity.class);
    //         List<MeetingPlaceEntity> lists = meetEntity.getLists();
    //         Map<String, Integer> placeCount = new HashMap<>();

    //         return null;
    //     } else {
    //         throw new RuntimeException("Document with mid " + mid + " not found");
    //     }
    // }

    // public List<PlaceDto> getPreferredPlaceByCategory(int mid, String category) throws InterruptedException, ExecutionException {
    //     Firestore db = FirestoreClient.getFirestore();
    //     CollectionReference meetingsRef = db.collection(COLLECTION_NAME);
    //     Query query = meetingsRef.whereEqualTo("mid", mid);
    //     ApiFuture<QuerySnapshot> querySnapshot = query.get();
    //     List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

    //     if (!documents.isEmpty()) {
    //         DocumentSnapshot document = documents.get(0);
    //         MeetEntity meetEntity = document.toObject(MeetEntity.class);
    //         List<MeetingPlaceEntity> lists = meetEntity.getLists();
    //         Map<String, Integer> placeCount = new HashMap<>();

    //         return null;
    //     } else {
    //         throw new RuntimeException("Document with mid " + mid + " not found");
    //     }
    // }

    public List<PlaceDto> getPlaceByPlaceNameAndMid(int mid, String placeName) throws ExecutionException, InterruptedException {
        if (placeName.isEmpty() || placeName.isBlank()) {
            return new ArrayList<>();
        }

        Firestore db = FirestoreClient.getFirestore();
        // Fetch all places
        ApiFuture<QuerySnapshot> future = db.collection(PLACE_COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<PlaceDto> places = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            PlaceDto place = document.toObject(PlaceDto.class);
            if (isSimilar(place.getPlaceName(), placeName) || containsWord(place.getPlaceName(), placeName)) {
                places.add(place);
            }
        }

        // Fetch members' UIDs
        MeetingDto meetingDto = getMeetingsByMid(mid);

        List<UsersDto> membersUid = meetingDto.getJoiners();
        List <String> members = new ArrayList<>();
        for(UsersDto user : membersUid) {
            members.add(user.getUid());
        }

        // Fetch places saved by members
        List<PlaceDto> memberPlaces = new ArrayList<>();
        PlaceListDao placeListDao = new PlaceListDao();
        for (String uid : members) {
            List<PlaceListDto> UsersPlaceList = placeListDao.getUserPlaceLists(uid);
            for (PlaceListDto placeList : UsersPlaceList) {
                List<PlaceDto> placeDtos = placeList.getPlaces();
                for (PlaceDto place : placeDtos) {
                    memberPlaces.add(place);
                }
            }
        }

        return sortPlacesBySimilarity(places, memberPlaces);
    }

    public List<PlaceDto> getPlaceByCategoryAndMid(int mid, String category) throws ExecutionException, InterruptedException {
        if (category.isBlank() || category.isEmpty()) {
            return new ArrayList<>();
        }

        Firestore db = FirestoreClient.getFirestore();
        // Fetch all places
        ApiFuture<QuerySnapshot> future = db.collection(PLACE_COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<PlaceDto> places = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            PlaceDto place = document.toObject(PlaceDto.class);
            if (isSimilar(place.getCategory(), category) || containsWord(place.getCategory(), category)) {
                places.add(place);
            }
        }

        // Fetch members' UIDs
        MeetingDto meetingDto = getMeetingsByMid(mid);

        List<UsersDto> membersUid = meetingDto.getJoiners();
        List <String> members = new ArrayList<>();
        for(UsersDto user : membersUid) {
            members.add(user.getUid());
        }

        // Fetch places saved by members
        List<PlaceDto> memberPlaces = new ArrayList<>();
        PlaceListDao placeListDao = new PlaceListDao();
        for (String uid : members) {
            List<PlaceListDto> UsersPlaceList = placeListDao.getUserPlaceLists(uid);
            for (PlaceListDto placeList : UsersPlaceList) {
                List<PlaceDto> placeDtos = placeList.getPlaces();
                for (PlaceDto place : placeDtos) {
                    memberPlaces.add(place);
                }
            }
        }

        return sortPlacesBySimilarity(places, memberPlaces);
    }

    private boolean isSimilar(String source, String target) {
        int distance = getLevenshteinDistance(source, target);
        double similarity = 1.0 - ((double) distance / Math.max(source.length(), target.length()));
        return similarity >= SIMILARITY_THRESHOLD;
    }

    private int getLevenshteinDistance(String source, String target) {
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        return levenshteinDistance.apply(source, target);
    }

    private boolean containsWord(String source, String target) {
        return source.toLowerCase().contains(target.toLowerCase());
    }

    private double getSimilarity(String source, String target) {
        int distance = getLevenshteinDistance(source, target);
        return 1.0 - ((double) distance / Math.max(source.length(), target.length()));
    }

    private List<PlaceDto> sortPlacesBySimilarity(List<PlaceDto> places, List<PlaceDto> targetPlaces) {
        return places.stream()
                .sorted((place1, place2) -> {
                    double maxSimilarity1 = getMaxSimilarity(place1.getPlaceName(), targetPlaces);
                    double maxSimilarity2 = getMaxSimilarity(place2.getPlaceName(), targetPlaces);
                    return Double.compare(maxSimilarity2, maxSimilarity1); // 유사성이 높은 순으로 정렬
                })
                .collect(Collectors.toList());
    }
    
    private double getMaxSimilarity(String source, List<PlaceDto> targetPlaces) {
        return targetPlaces.stream()
                .mapToDouble(target -> getSimilarity(source, target.getPlaceName()))
                .max()
                .orElse(0.0);
    }
    

    
}