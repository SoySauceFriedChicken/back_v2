package com.example.Foodle.controller;

import java.util.List;
import java.util.Map;
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

import com.example.Foodle.dto.request.meeting.MeetingDto;
import com.example.Foodle.dto.request.meeting.NewMeetingDto;
import com.example.Foodle.dto.request.meeting.UpdateMeeingTimeDto;
import com.example.Foodle.dto.request.meeting.UpdateMeetingDto;
import com.example.Foodle.dto.request.meetingPlace.MeetingPlaceDto;
import com.example.Foodle.dto.request.place.PlaceDto;
import com.example.Foodle.dto.request.user.UsersDto;
import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.entity.UsersEntity;
import com.example.Foodle.service.MeetingService;
import com.example.Foodle.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.HashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings")
public class MeetingController {
    @Autowired
    private MeetingService meetingService;

    @GetMapping
    public List<MeetingDto> getAllMeetings() throws ExecutionException, InterruptedException {
        return meetingService.getAllMeetings();
    }

    @GetMapping("/byUid")
    public List<MeetingDto> getMeetingsByName(@RequestParam String uid) throws ExecutionException, InterruptedException {
        return meetingService.getMeetingsByUid(uid);
    }

    @GetMapping("/byMid")
    public MeetingDto getMeetingsByMid(@RequestParam int mid) throws ExecutionException, InterruptedException {
        return meetingService.getMeetingsByMid(mid);
    }
    

    @PostMapping(path = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, Object>> createMeeting(@RequestBody @Valid NewMeetingDto newMeet) {
        MeetEntity meet = newMeet.toEntity();
        Map<String, Object> response = new HashMap<>();
        try {
            String result = meetingService.saveMeet(meet);
            if ("Meeting created successfully!".equals(result)) {
                response.put("success", true);
                response.put("error", null);
                response.put("message", result);
                response.put("status", HttpStatus.OK.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else if ("joiners is null".equals(result)) {
                response.put("success", false);
                response.put("error", "Joiners are null");
                response.put("message", result);
                response.put("status", HttpStatus.BAD_REQUEST.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            } else {
                response.put("success", false);
                response.put("error", "Unexpected error");
                response.put("message", result);
                response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Error creating user");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("data", new HashMap<>());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> updateMeeting(@RequestBody @Valid UpdateMeetingDto entity) {
        MeetEntity meet = entity.toEntity();
        Map<String, Object> response = new HashMap<>();
        try {
            String result = meetingService.updateMeet(meet);
            if (("Meeting with mid " + meet.getMid() + " updated successfully!").equals(result)) {
                response.put("success", true);
                response.put("error", null);
                response.put("message", result);
                response.put("status", HttpStatus.OK.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else if (("Document with mid " + meet.getMid() + " not found").equals(result)) {
                response.put("success", false);
                response.put("error", "Document not found");
                response.put("message", result);
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                response.put("success", false);
                response.put("error", "Unexpected error");
                response.put("message", result);
                response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Error updating meeting");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("data", new HashMap<>());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update/updatePlace")
    public ResponseEntity<Map<String, Object>> addPlaceList(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            int mid = (int) request.get("mid");
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> placesMapList = (List<Map<String, Object>>) request.get("places");

            List<MeetingPlaceDto> meetplace = new ArrayList<>();
            for (Map<String, Object> placeMap : placesMapList) {
                MeetingPlaceDto placeDto = objectMapper.convertValue(placeMap, MeetingPlaceDto.class);
                meetplace.add(placeDto);
            }

            // 서비스 메서드 호출
            String result = meetingService.addPlaceList(mid, meetplace);
            if ("Meeting places Updated successfully!".equals(result)) {
                response.put("success", true);
                response.put("error", null);
                response.put("message", result);
                response.put("status", HttpStatus.OK.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else if (("Document with mid " + mid + " not found").equals(result)) {
                response.put("success", false);
                response.put("error", "Document not found");
                response.put("message", result);
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                response.put("success", false);
                response.put("error", "Unexpected error");
                response.put("message", result);
                response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Error adding place list");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("data", new HashMap<>());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @PostMapping("/update/updatePlace")
    // public ResponseEntity<String> updatePlaceFromMeeting(@RequestBody Map<String, Object> request) {
    //     try {
    //         int mid = (int) request.get("mid");
    //         List<MeetingPlaceDto> meetplace = (List<MeetingPlaceDto>) request.get("places");

    //         // 서비스 메서드 호출
    //         meetingService.updatePlaceList(mid, meetplace);

    //         return ResponseEntity.ok("Place list updated successfully");
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating place list");
    //     }
    // }

    // @PostMapping("/update/deletePlace")
    // public ResponseEntity<String> deletePlaceFromMeeting(@RequestBody Map<String, Object> request) {
    //     try {
    //         int mid = (int) request.get("mid");
    //         List<MeetingPlaceDto> meetplace = (List<MeetingPlaceDto>) request.get("places");

    //         // 서비스 메서드 호출
    //         meetingService.deletePlaceList(mid, meetplace);

    //         return ResponseEntity.ok("Place list updated successfully");
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating place list");
    //     }
    // }
    
    

    @PostMapping("/update/updateJoiner")
    public ResponseEntity<Map<String, Object>> addUserToMeeting(@RequestBody Map<String, Object> entity) {
        Map<String, Object> response = new HashMap<>();
        try {
            int mid = (int) entity.get("mid");
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> joiners = (List<Map<String, Object>>) entity.get("joiners");

            List<UsersDto> newUsers = new ArrayList<>();
            for (Map<String, Object> joiner : joiners) {
                UsersDto joinerEntity = objectMapper.convertValue(joiner, UsersDto.class);
                newUsers.add(joinerEntity);
            }
            // 서비스 메서드 호출
            String result = meetingService.addUserToMeeting(mid, newUsers);
<<<<<<< HEAD
            if ("Meeting created successfully!".equals(result)) {
=======
            if ("Joiner Updated successfully!".equals(result)) {
>>>>>>> b4fbbc6 ([feat] String 리턴값 Json 형태로 수정 + 장소 검색시 Joiner의 장소 리스트)
                response.put("success", true);
                response.put("error", null);
                response.put("message", result);
                response.put("status", HttpStatus.OK.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else if (("Document with mid " + mid + " not found").equals(result)) {
                response.put("success", false);
                response.put("error", "Document not found");
                response.put("message", result);
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                response.put("success", false);
                response.put("error", "Unexpected error");
                response.put("message", result);
                response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Error adding user to meeting");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("data", new HashMap<>());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update/deleteJoiner")
    public ResponseEntity<Map<String, Object>> deleteUserFromMeeting(@RequestBody Map<String, Object> entity) {
        Map<String, Object> response = new HashMap<>();
        try {
            int mid = (int) entity.get("mid");
            UsersDto user = new ObjectMapper().convertValue(entity.get("joiner"), UsersDto.class);
            // 서비스 메서드 호출
            String result = meetingService.deleteUserFromMeeting(mid, user);
<<<<<<< HEAD
            if ("Meeting updated successfully!".equals(result)) {
=======
            if ("Delete Joiner successfully!".equals(result)) {
>>>>>>> b4fbbc6 ([feat] String 리턴값 Json 형태로 수정 + 장소 검색시 Joiner의 장소 리스트)
                response.put("success", true);
                response.put("error", null);
                response.put("message", result);
                response.put("status", HttpStatus.OK.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else if (("Document with mid " + mid + " not found").equals(result)) {
                response.put("success", false);
                response.put("error", "Document not found");
                response.put("message", result);
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                response.put("success", false);
                response.put("error", "Unexpected error");
                response.put("message", result);
                response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Error deleting user from meeting");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("data", new HashMap<>());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update/updateTime")
    public ResponseEntity<Map<String, Object>> updateTime(@RequestBody UpdateMeeingTimeDto entity) {
        Map<String, Object> response = new HashMap<>();
        try {
            String result = meetingService.updateTime(entity.getMid(), entity.getTime());
<<<<<<< HEAD
            if ("Meeting updated successfully!".equals(result)) {
=======
            if ("Meeting Time Updated successfully!".equals(result)) {
>>>>>>> b4fbbc6 ([feat] String 리턴값 Json 형태로 수정 + 장소 검색시 Joiner의 장소 리스트)
                response.put("success", true);
                response.put("error", null);
                response.put("message", result);
                response.put("status", HttpStatus.OK.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else if (("Document with mid " + entity.getMid() + " not found").equals(result)) {
                response.put("success", false);
                response.put("error", "Document not found");
                response.put("message", result);
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                response.put("success", false);
                response.put("error", "Unexpected error");
                response.put("message", result);
                response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Error updating meeting time");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("data", new HashMap<>());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteMeeting(@RequestBody MeetingDto entity) {
        Map<String, Object> response = new HashMap<>();
        try {
            String result = meetingService.deleteMeeting(entity.toEntity());
            if ("Meeting deleted successfully!".equals(result)) {
                response.put("success", true);
                response.put("error", null);
                response.put("message", result);
                response.put("status", HttpStatus.OK.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else if (("Document with mid " + entity.getMid() + " not found").equals(result)) {
                response.put("success", false);
                response.put("error", "Document not found");
                response.put("message", result);
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                response.put("success", false);
                response.put("error", "Unexpected error");
                response.put("message", result);
                response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Error deleting meeting");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("data", new HashMap<>());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
<<<<<<< HEAD
=======

    // 미팅에 참여중인 참가자들 목록을 가져와서 참가자들이 선호하는 장소 리턴하기
    @GetMapping("/getPreferredPlacebyPlaceName")
    public List<PlaceDto> getPreferredPlaceByPlaceName(@RequestParam int mid, @RequestParam String placeName) {
        try {
            List<PlaceDto> result = meetingService.getPreferredPlaceByPlaceName(mid, placeName);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/getPreferredPlacebyCategory")
    public List<PlaceDto> getPreferredPlaceByCategory(@RequestParam int mid, @RequestParam String category) {
        // log.info("mid: " + mid + ", category: " + category);
        try {
            // log.info("mid: " + mid + ", category: " + category);
            List<PlaceDto> result = meetingService.getPreferredPlaceByCategory(mid, category);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            // log.info("mid: " + mid + ", category: " + category);
            return null;
        }
    }
>>>>>>> b4fbbc6 ([feat] String 리턴값 Json 형태로 수정 + 장소 검색시 Joiner의 장소 리스트)
}