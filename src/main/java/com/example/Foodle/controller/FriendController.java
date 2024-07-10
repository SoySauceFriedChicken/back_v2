package com.example.Foodle.controller;

import java.util.Arrays;
import java.util.List;
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
    public String createFriend(@RequestParam String uid, @RequestParam String fid)  throws ExecutionException, InterruptedException {
        try {
            friendService.createFriend(uid, fid);
            return "Friend created successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error creating friend";
        }
    }

    @PostMapping("/Update")
    public String updateFriend(@RequestParam String uid, @RequestParam String fid)  throws ExecutionException, InterruptedException {
        try {
            friendService.updateFriend(uid, fid);
            return "Friend updated successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error updating friend";
        }
    }

    // @GetMapping("/test")
    // public FriendEntity getFriend(@RequestParam int uid)  throws ExecutionException, InterruptedException {
    //     return friendService.getFriendWithUser(uid);
    // }
}