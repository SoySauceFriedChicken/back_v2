package com.example.Foodle.service;

import com.example.Foodle.dao.MeetingDao;
import com.example.Foodle.dto.request.friend.FriendDto;
import com.example.Foodle.dto.request.meeting.MeetingDto;
import com.example.Foodle.dto.request.meetingPlace.MeetingPlaceDto;
import com.example.Foodle.dto.request.place.PlaceDto;
import com.example.Foodle.dto.request.user.UsersDto;
import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.entity.UsersEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.Date;

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

    public String saveMeet(MeetEntity meet) throws InterruptedException, ExecutionException {
        return meetingDao.saveMeet(meet);
    }

    public String updateMeet(MeetEntity meet) throws InterruptedException, ExecutionException, IOException {
        return meetingDao.updateMeet(meet);
    }

    public String addPlaceList(int mid, List<MeetingPlaceDto> meetplace) throws InterruptedException, ExecutionException, IOException {
        return meetingDao.addPlaceList(mid, meetplace);
    }

    // public String updatePlaceList(int mid, List<MeetingPlaceDto> meetplace) throws InterruptedException, ExecutionException {
    //     return meetingDao.addPlaceList(mid, meetplace);
    // }
    // public String deletePlaceList(int mid, List<MeetingPlaceDto> meetplace) throws InterruptedException, ExecutionException {
    //     return meetingDao.addPlaceList(mid, meetplace);
    // }

    public String addUserToMeeting(int mid, List<UsersDto>joiners) throws InterruptedException, ExecutionException, IOException {
        return meetingDao.addUserToMeeting(mid, joiners);
    }

    public String deleteUserFromMeeting(int mid, UsersDto joiner) throws InterruptedException, ExecutionException {
        return meetingDao.deleteUserFromMeeting(mid, joiner);
    }

    public String updateTime(int mid, Date time) throws InterruptedException, ExecutionException, IOException {
        return meetingDao.updateTime(mid, time);
    }

    public String deleteMeeting(MeetEntity meetEntity) throws InterruptedException, ExecutionException {
        return meetingDao.deleteMeeting(meetEntity);
    }

    public List<PlaceDto> getPreferredPlaceByPlaceName(MeetingDto meeting, String placeName) throws InterruptedException, ExecutionException, IOException {
        return meetingDao.getPlaceByPlaceNameAndMid(meeting, placeName);
    }

    public List<PlaceDto> getPreferredPlaceByCategory(MeetingDto meeting, String category) throws InterruptedException, ExecutionException, IOException {
        return meetingDao.getPlaceByCategoryAndMid(meeting, category);
    }

}