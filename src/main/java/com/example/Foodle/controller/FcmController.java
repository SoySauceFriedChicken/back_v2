package com.example.Foodle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.Foodle.service.FirestoreService;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/fcm")
public class FcmController {

    @Autowired
    private FirestoreService firestoreService;

    @PostMapping("/token")
    public void saveFcmToken(@RequestParam String uid, @RequestParam String fcmToken) {
        try {
            firestoreService.saveFcmToken(uid, fcmToken);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save FCM token");
        }
    }

    @GetMapping("/token")
    public String getFcmToken(@RequestParam String uid) {
        try {
            return firestoreService.getFcmToken(uid);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get FCM token");
        }
    }
}
