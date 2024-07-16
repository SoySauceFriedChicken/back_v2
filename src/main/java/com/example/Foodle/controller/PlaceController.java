package com.example.Foodle.controller;


import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Foodle.dto.request.place.PlaceDto;
import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.entity.PlaceEntity;
import com.example.Foodle.entity.UsersEntity;
import com.example.Foodle.service.MeetingService;
import com.example.Foodle.service.PlaceService;
import com.example.Foodle.service.UsersService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/place")
@Slf4j
public class PlaceController {
    @Autowired
    private PlaceService placeService;

    @GetMapping
    public List<PlaceDto> getAllPlaces() throws ExecutionException, InterruptedException {
        return placeService.getAllPlaces();
    }

    @GetMapping("/byPlaceName")
    public List<PlaceDto> getPlacesByName(@RequestParam String placeName, Double latitude, Double longitude) throws ExecutionException, InterruptedException {
        //log.info("placeName: " + placeName);
        return placeService.getPlaceByPlaceName(placeName);
    }

    @GetMapping("/byPlaceInfo")
    public List<PlaceDto> getPlacesByInfo(@RequestParam String placeName, Double latitude, Double longitude) throws ExecutionException, InterruptedException {
        //log.info("placeName: " + placeName);
        return placeService.getPlaceByPlaceInfo(placeName, latitude, longitude);
    }

    @GetMapping("/byCategory")
    public List<PlaceDto> getPlacesByCategory(@RequestParam String category) throws ExecutionException, InterruptedException {
        //log.info("placeName: " + category);
        return placeService.getPlacesByCategory(category);
    }

}
