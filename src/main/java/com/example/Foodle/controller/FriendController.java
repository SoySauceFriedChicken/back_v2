package com.example.Foodle.controller;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Foodle.entity.FriendEntity;
import com.example.Foodle.service.FriendService;

import org.springframework.beans.factory.annotation.Autowired;
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
    public List<FriendEntity> getFriendsByUid(@RequestParam int uid) throws ExecutionException, InterruptedException {
        return friendService.getFriendsByUid(uid);
        
    }

    // @GetMapping("/test")
    // public FriendEntity getFriend(@RequestParam int uid)  throws ExecutionException, InterruptedException {
    //     return friendService.getFriendWithUser(uid);
    // }
}