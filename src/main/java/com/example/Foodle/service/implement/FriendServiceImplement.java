package com.example.Foodle.service.implement;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.example.Foodle.dao.FriendDao;
import com.example.Foodle.dao.MeetingDao;
import com.example.Foodle.entity.FriendEntity;
import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.service.FriendService;
import com.example.Foodle.service.MeetingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendServiceImplement extends FriendService {
    private final FriendDao friendDao;

    @Override
    public List<FriendEntity> getFriendsByUid(String uid) throws ExecutionException, InterruptedException {
        return friendDao.getFriendsByUid(uid);
    }

}
