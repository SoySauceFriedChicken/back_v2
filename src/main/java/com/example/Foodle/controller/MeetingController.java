package com.example.Foodle.controller;

import java.util.List;
import java.util.Map;
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
import com.example.Foodle.dto.request.meeting.UpdateMeetingDto;
import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.entity.UsersEntity;
import com.example.Foodle.service.MeetingService;
import com.example.Foodle.service.UsersService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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
    public MeetingDto getMeetingsByMid(@RequestParam String mid) throws ExecutionException, InterruptedException {
        return meetingService.getMeetingsByMid(mid);
    }
    

    @PostMapping(path = "/create", consumes = "application/json", produces = "application/json")
    public String createMeeting(@RequestBody @Valid NewMeetingDto newMeet) {
        MeetEntity meet = newMeet.toEntity();
        try {
            meetingService.saveMeet(meet);
            return "Meeting created successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error creating user";
        }
    }

    @PostMapping("/update")
    public String updateMeeting(@RequestBody @Valid UpdateMeetingDto entity) {
        MeetEntity meet = entity.toEntity();
        try {
            meetingService.updateMeet(meet);
            return "Meeting updated successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error creating user";
        }
    }

    @PostMapping("/update/addPlace")
    public ResponseEntity<String>addPlaceList(@RequestBody Map<String, Object> request) {
        try {
            String mid = (String) request.get("mid");
            Map<String, Object> meetplace = (Map<String, Object>) request.get("meetplace");

            // 서비스 메서드 호출
            meetingService.addPlaceList(mid, meetplace);

            return ResponseEntity.ok("Place list updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating place list");
        }
    }

    @PostMapping("/update/updatePlace")
    public String updatePlaceFromMeeting(@RequestBody String entity) {
        return entity;

    }

    @PostMapping("/update/deletePlace")
    public String deletePlaceFromMeeting(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
    
    @PostMapping("/update/addUser")
    public String addUserToMeeting(@RequestBody String entity) {
        return entity;
        
    }


    
    

}
