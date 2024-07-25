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
import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.entity.UsersEntity;
import com.example.Foodle.service.MeetingService;
import com.example.Foodle.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;


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
    public String createMeeting(@RequestBody @Valid NewMeetingDto newMeet) {
        MeetEntity meet = newMeet.toEntity();
        try {
            return meetingService.saveMeet(meet);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error creating user";
        }
    }

    @PostMapping("/update")
    public String updateMeeting(@RequestBody @Valid UpdateMeetingDto entity) {
        MeetEntity meet = entity.toEntity();
        try {
            return meetingService.updateMeet(meet);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error creating user";
        }
    }

    @PostMapping("/update/updatePlace")
    public String addPlaceList(@RequestBody Map<String, Object> request) {
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
            return meetingService.addPlaceList(mid, meetplace);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error adding place list";
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
    
    // 약속에 유저 추가 + (삭제)
    @PostMapping("/update/updateJoiner")
    public String addUserToMeeting(@RequestBody Map<String, Object> entity) {
        try {
            int mid = (int) entity.get("mid");
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> joiners = (List<Map<String, Object>>) entity.get("joiners");

            List<UsersEntity> newUsers = new ArrayList<>();
            for (Map<String, Object> joiner : joiners) {
                UsersEntity joinerEntity = objectMapper.convertValue(joiner, UsersEntity.class);
                newUsers.add(joinerEntity);
            }
            // 서비스 메서드 호출
            return meetingService.addUserToMeeting(mid, newUsers);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "Error adding user to meeting";
        }
        
        
    }

    // 약속 시간 수정
    @PostMapping("/update/updateTime")
    public String updateTime(@RequestBody UpdateMeeingTimeDto entity) {
        try {
            return meetingService.updateTime(entity.getMid(), entity.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "Error updating meeting time";
        }
    }

    // 약속 삭제
    @PostMapping("/delete")
    public String deleteMeeting(@RequestBody MeetingDto entity) {
        try {
            return meetingService.deleteMeeting(entity.toEntity());
        } catch (Exception e) {
            e.printStackTrace();
            return "Error deleting meeting";
        }
    }
    
    

}
