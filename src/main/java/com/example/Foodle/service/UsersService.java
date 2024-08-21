package com.example.Foodle.service;


import com.example.Foodle.dao.UsersDao;
import com.example.Foodle.dto.request.user.UsersDto;
import com.example.Foodle.entity.UsersEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.lang.System.in;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
public class UsersService {

    @Autowired
    private UsersDao usersDao;

    private static final String COLLECTION_NAME = "Users";

    private Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }

    public List<UsersDto> getAllUsers() throws ExecutionException, InterruptedException {
        return usersDao.getUsers();
    }

    public UsersDto findByUid(String uid) throws InterruptedException, ExecutionException {
        return usersDao.findByUid(uid);
    }

    public String saveUser(UsersEntity user) throws InterruptedException, ExecutionException {
        return usersDao.saveUser(user);
    }

    public String updateUser(UsersDto user) {
        return usersDao.updateUser(user);
    }
    
    public String deleteUser(String uid) {
        return usersDao.deleteUser(uid);
    }

    public String getFriendCode(String uid) throws InterruptedException, ExecutionException {
        return usersDao.getFriendCode(uid);
    }   
}