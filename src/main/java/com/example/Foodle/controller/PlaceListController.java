package com.example.Foodle.controller;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
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
    public ResponseEntity<Map<String, Object>> createPlaceList(@RequestBody @Valid PlaceListDto placeListDto) {
        PlaceListEntity placeList = placeListDto.toEntity();
        Map<String, Object> response = new HashMap<>();
        try {
            String result = placeListService.createPlaceList(placeList);
            response.put("success", true);
            response.put("error", null);
            response.put("message", result);
            response.put("status", HttpStatus.OK.value());
            response.put("data", new HashMap<>());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Error creating PlaceList");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("data", new HashMap<>());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> updatePlaceList(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
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
            String result = placeListService.updatePlaceList(lid, placeList);
            response.put("success", true);
            response.put("error", null);
            response.put("message", "Place list updated successfully");
            response.put("status", HttpStatus.OK.value());
            response.put("data", new HashMap<>());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Error updating place list");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("data", new HashMap<>());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<Map<String, Object>> deletePlaceList(@RequestBody PlaceListDto placeListDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            String result = placeListService.deletePlaceList(placeListDto.toEntity());
            response.put("success", true);
            response.put("error", null);
            response.put("message", result);
            response.put("status", HttpStatus.OK.value());
            response.put("data", new HashMap<>());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Error deleting place list");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("data", new HashMap<>());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
}
