package com.example.Foodle.service;


import com.example.Foodle.dao.MeetingDao;
import com.example.Foodle.dao.PlaceDao;
import com.example.Foodle.dao.PlaceListDao;
import com.example.Foodle.dto.request.placeList.PlaceListDto;
import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.entity.PlaceEntity;
import com.example.Foodle.entity.PlaceListEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class PlaceListService {
    @Autowired
    private PlaceListDao placeListDao;

    private static final String COLLECTION_NAME = "PlaceList";

    private Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }

    public List<PlaceListDto> getUserPlaceLists(String uid) throws ExecutionException, InterruptedException {
        return placeListDao.getUserPlaceLists(uid);
    }
    public List<PlaceListDto> getPlaceListByLid(int lid) throws ExecutionException, InterruptedException {
        return placeListDao.getPlaceListByLid(lid);
    }
}
