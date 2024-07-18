package com.example.Foodle.service.implement;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.example.Foodle.dao.UsersDao;
import com.example.Foodle.dto.request.user.UsersDto;
import com.example.Foodle.entity.UsersEntity;
import com.example.Foodle.service.UsersService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersServiceImplement extends UsersService {

    private final UsersDao usersDao;

    @Override
    public List<UsersDto> getAllUsers() throws ExecutionException, InterruptedException {
        return usersDao.getUsers();
    }

    @Override // Add the @Override annotation
    public UsersDto findByUid(String uid) throws ExecutionException, InterruptedException {
        return usersDao.findByUid(uid);
    }
}