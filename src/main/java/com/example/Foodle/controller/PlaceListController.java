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

import com.example.Foodle.entity.PlaceListEntity;
import com.example.Foodle.service.PlaceListService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/placeList")
public class PlaceListController {
    @Autowired
    private PlaceListService placeListService;

    @GetMapping("/byUid")
    public List<PlaceListEntity> getAllMeetings(@RequestParam int uid) throws ExecutionException, InterruptedException {
        return placeListService.getUserPlaceLists(uid);
    }

    @GetMapping("/byLid")
    public List<PlaceListEntity> getMeetingsByName(@RequestParam int lid) throws ExecutionException, InterruptedException {
        return placeListService.getPlaceListByLid(lid);
    }
}
