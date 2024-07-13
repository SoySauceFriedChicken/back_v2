package com.example.Foodle.dao;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Repository;

import com.example.Foodle.dto.request.meeting.MeetingDto;
import com.example.Foodle.dto.request.meetingPlace.MeetingPlaceInfoDto;
import com.example.Foodle.dto.request.place.PlaceDto;
import com.example.Foodle.dto.request.placeList.PlaceListDto;
import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.entity.MeetingPlaceEntity;
import com.example.Foodle.entity.MeetingPlaceInfoEntity;
import com.example.Foodle.entity.PlaceEntity;
import com.example.Foodle.entity.PlaceListEntity;
import com.example.Foodle.entity.UsersEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import java.awt.Color;

import java.awt.geom.Point2D;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class PlaceListDao {
    public static final String COLLECTION_NAME = "PlaceList";
    public static final String PLACE_COLLECTION_NAME = "Place";


    public List<PlaceListDto> getUserPlaceLists(String uid) throws ExecutionException, InterruptedException {
        List<PlaceListDto> list = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        return convertToMeetingDtos(db, documents);
    }

    public List<PlaceListDto> getPlaceListByLid(int lid) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).whereEqualTo("lid", lid).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        return convertToMeetingDtos(db, documents);
    }

    private List<PlaceListDto> convertToMeetingDtos(Firestore db, List<QueryDocumentSnapshot> documents) throws ExecutionException, InterruptedException {
        List<PlaceListDto> meetingDtos = new ArrayList<>();

        for (QueryDocumentSnapshot document : documents) {
            PlaceListEntity placeListEntity = document.toObject(PlaceListEntity.class);

            List<MeetingPlaceInfoEntity> lists = placeListEntity.getPlaces() != null ? placeListEntity.getPlaces() : new ArrayList<>();
            List<PlaceDto> placeLists = new ArrayList<>();

            for (MeetingPlaceInfoEntity list : lists) {
    
                Query placeRef = db.collection(PLACE_COLLECTION_NAME)
                                   .whereEqualTo("placeName", list.getPlaceName())
                                   .whereEqualTo("latitude", list.getLatitude())
                                   .whereEqualTo("longitude", list.getLongitude());
                ApiFuture<QuerySnapshot> placeSnapshotFuture = placeRef.get();
                QuerySnapshot placeSnapshot = placeSnapshotFuture.get();
    
                if (!placeSnapshot.isEmpty()) {
                    placeLists.add(placeSnapshot.getDocuments().get(0).toObject(PlaceDto.class));
                    // log.info("Place found: " + placeData);
                } else {
                    log.info("Place with pid " + list + " not found");
                }
            }

            PlaceListDto placeListDto  = new PlaceListDto(
                placeListEntity.getLid(),
                placeListEntity.getUid(),
                placeListEntity.getName(),
                placeListEntity.getColor(),
                placeLists
            );

            meetingDtos.add(placeListDto);
            // log.info("Meeting found: " + meetingDto);
        }

        return meetingDtos;
    }

    public void createPlaceList(PlaceListEntity placeList) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                                             .orderBy("lid", Query.Direction.DESCENDING)
                                             .limit(1)
                                             .get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        int size;
        if (!documents.isEmpty()) {
            QueryDocumentSnapshot document = documents.get(0);
            size = document.getLong("lid").intValue();
        } else {
            size =  0; // 컬렉션이 비어있다면 0을 반환
        }
        log.info("size: " + size);

        placeList.setLid(size + 1);
        log.info("Saving meeting with lid " + placeList.getLid());
        db.collection(COLLECTION_NAME).document().set(placeList);
    }

    public void updatePlaceList(int lid, List<PlaceDto> placeList) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference placeRef = db.collection(COLLECTION_NAME);
        Query query = placeRef.whereEqualTo("lid", lid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        if(!documents.isEmpty()) {
            DocumentSnapshot document = documents.get(0);
            PlaceListEntity placeListEntity = document.toObject(PlaceListEntity.class);

            List<MeetingPlaceInfoEntity> places = new ArrayList<>();
            for (PlaceDto place : placeList) {
                MeetingPlaceInfoEntity placeInfo = new MeetingPlaceInfoEntity(
                    place.getPlaceName(),
                    place.getLatitude(),
                    place.getLongitude()
                );
                places.add(placeInfo);
            }
            placeListEntity.setPlaces(places);
            document.getReference().set(placeListEntity);
        }
        else {
            log.info("PlaceList with lid " + lid + " not found");
        }
    }

    public void deletePlaceList(PlaceListEntity placeList) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference placeRef = db.collection(COLLECTION_NAME);
        Query query = placeRef.whereEqualTo("lid", placeList.getLid());
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        if(!documents.isEmpty()) {
            DocumentSnapshot document = documents.get(0);
            document.getReference().delete();
        }
        else {
            log.info("PlaceList with lid " + placeList.getLid() + " not found");
        }
    }
}
