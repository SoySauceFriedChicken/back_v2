package com.example.Foodle.service;

import com.example.Foodle.dao.MeetingDao;
import com.example.Foodle.dto.request.friend.FriendDto;
import com.example.Foodle.dto.request.meeting.MeetingDto;
import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.entity.UsersEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
public class MeetingService {

    @Autowired
    private MeetingDao meetingDao;
    private MeetingDto meetDto;

    public List<MeetingDto> getAllMeetings() throws ExecutionException, InterruptedException {
        return meetingDao.getMeeting();
    }

    public List<MeetingDto> getMeetingsByUid(String uid) throws ExecutionException, InterruptedException {
        return meetingDao.getMeetingsByUid(uid);
    }

    public MeetingDto getMeetingsByMid(String mid) throws ExecutionException, InterruptedException {
        return meetingDao.getMeetingsByMid(mid);
    }

    public void saveMeet(MeetEntity meet) {
        meetingDao.saveMeet(meet);
    }

    public void updateMeet(MeetEntity meet) throws InterruptedException, ExecutionException {
        meetingDao.updateMeet(meet);
    }

    public void addPlaceList(String mid, List<Map<String, Object>> meetplace) throws InterruptedException, ExecutionException {
        meetingDao.addPlaceList(mid, meetplace);
    }

}