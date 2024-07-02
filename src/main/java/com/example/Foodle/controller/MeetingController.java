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

import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.entity.UsersEntity;
import com.example.Foodle.service.MeetingService;
import com.example.Foodle.service.UsersService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings")
public class MeetingController {
    @Autowired
    private MeetingService meetingService;

    @GetMapping
    public List<MeetEntity> getAllMeetings() throws ExecutionException, InterruptedException {
        return meetingService.getAllMeetings();
    }

    @GetMapping("/byUid")
    public List<MeetEntity> getMeetingsByName(@RequestParam int uid) throws ExecutionException, InterruptedException {
        return meetingService.getMeetingsByUid(uid);
    }

    @GetMapping("/byMid")
    public List<MeetEntity> getMethodName(@RequestParam int mid) throws ExecutionException, InterruptedException {
        return meetingService.getMeetingsByMid(mid);
    }
    

}
