package com.example.Foodle.service;


import com.example.Foodle.dao.MeetingDao;
import com.example.Foodle.dao.PlaceDao;
import com.example.Foodle.dto.request.place.PlaceDto;
import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.entity.PlaceEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PlaceService {

    @Autowired
    private PlaceDao placeDao;

    private static final String COLLECTION_NAME = "Place";

    private Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }

    public List<PlaceDto> getAllPlaces() throws ExecutionException, InterruptedException {
        return placeDao.getAllPlaces();
    }
    public List<PlaceDto> getPlaceByPlaceName(String placeName) throws ExecutionException, InterruptedException {
        return placeDao.getPlaceByPlaceName(placeName);    
    }
    public List<PlaceDto> getPlaceByPlaceInfo(String placeName, Double latitude, Double longitude) throws ExecutionException, InterruptedException {
        return placeDao.getPlaceByPlaceInfo(placeName, latitude, longitude);    
    }
}
