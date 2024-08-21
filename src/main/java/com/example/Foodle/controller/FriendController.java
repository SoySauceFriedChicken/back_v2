package com.example.Foodle.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Foodle.dto.request.friend.FriendDto;
import com.example.Foodle.entity.FriendEntity;
import com.example.Foodle.service.FriendService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    private final FriendService friendService;

    @Autowired
    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping("/byUid")
    public ResponseEntity getFriendsByUid(@RequestParam String uid) throws ExecutionException, InterruptedException {
        try {
            List<FriendDto> friends = friendService.getFriendsWithUserDetails(uid);
            return ResponseEntity.ok(friends);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/Create")
    public ResponseEntity<Map<String, Object>> createFriend(@RequestParam String uid, @RequestParam String fid) throws ExecutionException, InterruptedException {
        Map<String, Object> response = new HashMap<>();
        try {
            friendService.createFriend(uid, fid);
            response.put("success", true);
            response.put("error", null);
            response.put("message", "Friend created successfully");
            response.put("status", HttpStatus.OK.value());
            response.put("data", new HashMap<>());  // empty data object
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Error creating friend");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("data", new HashMap<>());  // empty data object
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/CreateByCode")
    public ResponseEntity<Map<String, Object>> createFriendByCodEntity(@RequestParam String uid, @RequestParam String code) throws ExecutionException, InterruptedException {
        Map<String, Object> response = new HashMap<>();
        try {
            String result = friendService.createFriendByCode(uid, code);

            if ("Already friend".equals(result)) {
                response.put("success", false);
                response.put("error", "Already friends");
                response.put("message", "The users are already friends");
                response.put("status", HttpStatus.CONFLICT.value());
                response.put("data", new HashMap<>());  // empty data object
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            } else if ("Friend added Successfully".equals(result)) {
                response.put("success", true);
                response.put("error", null);
                response.put("message", "Friend added successfully");
                response.put("status", HttpStatus.OK.value());
                response.put("data", new HashMap<>());  // empty data object
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("error", "Unknown error");
                response.put("message", "Unexpected result: " + result);
                response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.put("data", new HashMap<>());  // empty data object
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Error creating friend");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("data", new HashMap<>());  // empty data object
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/Update")
    public ResponseEntity<Map<String, Object>> updateFriend(@RequestParam String uid, @RequestParam String fid) throws ExecutionException, InterruptedException {
        Map<String, Object> response = new HashMap<>();
        try {
            friendService.updateFriend(uid, fid);
            response.put("success", true);
            response.put("error", null);
            response.put("message", "Friend updated successfully");
            response.put("status", HttpStatus.OK.value());
            response.put("data", new HashMap<>());  // empty data object
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Error updating friend");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("data", new HashMap<>());  // empty data object
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // @GetMapping("/test")
    // public FriendEntity getFriend(@RequestParam int uid)  throws ExecutionException, InterruptedException {
    //     return friendService.getFriendWithUser(uid);
    // }
}