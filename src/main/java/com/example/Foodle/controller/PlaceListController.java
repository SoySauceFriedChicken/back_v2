package com.example.Foodle.controller;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.example.Foodle.dto.request.placeList.PlaceListDto;
import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.entity.PlaceListEntity;
import com.example.Foodle.service.PlaceListService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/placeList")
public class PlaceListController {
    @Autowired
    private PlaceListService placeListService;

    @GetMapping("/byUid")
    public List<PlaceListDto> getAllPlaceLists(@RequestParam String uid) throws ExecutionException, InterruptedException {
        return placeListService.getUserPlaceLists(uid);
    }

    @GetMapping("/byLid")
    public List<PlaceListDto> getPlaceListByLid(@RequestParam int lid) throws ExecutionException, InterruptedException {
        return placeListService.getPlaceListByLid(lid);
    }

    @PostMapping("/create")
    public String createPlaceList(@RequestBody @Valid PlaceListDto placeListDto) {
        PlaceListEntity placeList = placeListDto.toEntity();
        try {
            placeListService.createPlaceList(placeList);
            return "PlaceList created successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error creating PlaceList";
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> updatePlaceList(@RequestBody Map<String, Object> request) {
        try {
            int lid = (int) request.get("lid");
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> placesMapList = (List<Map<String, Object>>) request.get("places");
    
            // Map을 PlaceDto 객체로 변환
            List<PlaceDto> placeList = new ArrayList<>();
            for (Map<String, Object> placeMap : placesMapList) {
                PlaceDto placeDto = objectMapper.convertValue(placeMap, PlaceDto.class);
                placeList.add(placeDto);
            }
    
            // 서비스 메서드 호출
            placeListService.updatePlaceList(lid, placeList);
            return ResponseEntity.ok("Place list updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating place list");
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deletePlaceList(@RequestBody PlaceListDto placeListDto) {
        try {
            placeListService.deletePlaceList(placeListDto.toEntity());
            return ResponseEntity.ok("Place list deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting place list");
        }
    }
    
    
}
