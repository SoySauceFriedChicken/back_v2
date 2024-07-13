package com.example.Foodle.service;

import com.example.Foodle.dao.MeetingDao;
import com.example.Foodle.dto.request.friend.FriendDto;
import com.example.Foodle.dto.request.meeting.MeetingDto;
import com.example.Foodle.dto.request.meetingPlace.MeetingPlaceDto;
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

    public MeetingDto getMeetingsByMid(int mid) throws ExecutionException, InterruptedException {
        return meetingDao.getMeetingsByMid(mid);
    }

    public void saveMeet(MeetEntity meet) throws InterruptedException, ExecutionException {
        meetingDao.saveMeet(meet);
    }

    public void updateMeet(MeetEntity meet) throws InterruptedException, ExecutionException {
        meetingDao.updateMeet(meet);
    }

    public void addPlaceList(int mid, List<MeetingPlaceDto> meetplace) throws InterruptedException, ExecutionException {
        meetingDao.addPlaceList(mid, meetplace);
    }

    public void updatePlaceList(int mid, List<MeetingPlaceDto> meetplace) throws InterruptedException, ExecutionException {
        meetingDao.addPlaceList(mid, meetplace);
    }
    public void deletePlaceList(int mid, List<MeetingPlaceDto> meetplace) throws InterruptedException, ExecutionException {
        meetingDao.addPlaceList(mid, meetplace);
    }

    public void addUserToMeeting(int mid, List<UsersEntity>joiners) throws InterruptedException, ExecutionException {
        meetingDao.addUserToMeeting(mid, joiners);
    }

    public void deleteMeeting(MeetEntity meetEntity) throws InterruptedException, ExecutionException {
        meetingDao.deleteMeeting(meetEntity);
    }

}