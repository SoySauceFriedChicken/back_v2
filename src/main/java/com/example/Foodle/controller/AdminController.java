package com.example.Foodle.controller;

import com.example.Foodle.dto.request.push.FCMMessageDto;
import com.example.Foodle.service.AdminService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final AdminService adminService;

    @PostMapping("/send-push")
    public void sendPushNotification(@RequestBody FCMMessageDto fcmMessageDto) throws IOException, JsonProcessingException {
        String targetToken = fcmMessageDto.getMessage().getToken();
        String title = fcmMessageDto.getMessage().getNotification().getTitle();
        String body = fcmMessageDto.getMessage().getNotification().getBody();
        String name = fcmMessageDto.getMessage().getData().getName();
        String description = fcmMessageDto.getMessage().getData().getDescription();

        adminService.sendPushMessage(targetToken, title, body, name, description);
    }
}
