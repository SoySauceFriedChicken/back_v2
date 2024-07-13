package com.example.Foodle.service.implement;


import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.example.Foodle.dao.MeetingDao;
import com.example.Foodle.dao.PlaceDao;
import com.example.Foodle.dto.request.place.PlaceDto;
import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.entity.PlaceEntity;
import com.example.Foodle.service.PlaceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaceServiceImplement extends PlaceService {

    private final PlaceDao placeDao;

    @Override
    public List<PlaceDto> getAllPlaces() throws ExecutionException, InterruptedException {
        return placeDao.getAllPlaces();
    }

    @Override // Add the @Override annotation
    public List<PlaceDto> getPlaceByPlaceName(String placeName, Double latitude, Double longitude) throws ExecutionException, InterruptedException {
        return placeDao.getPlaceByPlaceName(placeName, latitude, longitude);
    }

    
}