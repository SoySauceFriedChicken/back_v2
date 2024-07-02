package com.example.Foodle.service.implement;


import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.example.Foodle.dao.PlaceListDao;
import com.example.Foodle.entity.PlaceListEntity;
import com.example.Foodle.service.PlaceListService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaceListServiceImplement extends PlaceListService {

    private final PlaceListDao placeListDao;

    @Override
    public List<PlaceListEntity> getUserPlaceLists(int uid) throws ExecutionException, InterruptedException {
        return placeListDao.getUserPlaceLists(uid);
    }

    @Override
    public List<PlaceListEntity> getPlaceListByLid(int lid) throws ExecutionException, InterruptedException {
        return placeListDao.getPlaceListByLid(lid);
    }
    
}
