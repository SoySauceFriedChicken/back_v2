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
import com.example.Foodle.dto.request.place.PlaceDto;
import com.example.Foodle.dto.request.placeList.PlaceListDto;
import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.entity.MeetingPlaceEntity;
import com.example.Foodle.entity.MeetingPlaceInfoEntity;
import com.example.Foodle.entity.PlaceEntity;
import com.example.Foodle.entity.PlaceListEntity;
import com.example.Foodle.entity.UsersEntity;
import com.google.api.core.ApiFuture;
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
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
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
                placeListEntity.getName(),
                placeListEntity.getColor(),
                placeLists
            );

            meetingDtos.add(placeListDto);
            // log.info("Meeting found: " + meetingDto);
        }

        return meetingDtos;
    }
}
