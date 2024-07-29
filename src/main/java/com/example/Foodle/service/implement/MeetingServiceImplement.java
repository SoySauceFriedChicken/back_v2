package com.example.Foodle.service.implement;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.example.Foodle.dao.MeetingDao;
import com.example.Foodle.dto.request.meeting.MeetingDto;
import com.example.Foodle.dto.request.meetingPlace.MeetingPlaceDto;
import com.example.Foodle.dto.request.user.UsersDto;
import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.service.MeetingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MeetingServiceImplement extends MeetingService {

    private final MeetingDao meetingDao;

    @Override
    public List<MeetingDto> getAllMeetings() throws ExecutionException, InterruptedException {
        return meetingDao.getMeeting();
    }

    @Override // Add the @Override annotation
    public List<MeetingDto> getMeetingsByUid(String uid) throws ExecutionException, InterruptedException {
        return meetingDao.getMeetingsByUid(uid);
    }

    @Override // Add the @Override annotation
    public MeetingDto getMeetingsByMid(int mid) throws ExecutionException, InterruptedException {
        return meetingDao.getMeetingsByMid(mid);
    }

    @Override // Add the @Override annotation
    public String saveMeet(MeetEntity meet) throws InterruptedException, ExecutionException {
        return meetingDao.saveMeet(meet);
    }

    @Override // Add the @Override annotation
    public String updateMeet(MeetEntity meet) throws InterruptedException, ExecutionException, IOException {
        return meetingDao.updateMeet(meet);
    }

    @Override // Add the @Override annotation
    public String addPlaceList(int mid, List<MeetingPlaceDto> meetplace) throws InterruptedException, ExecutionException, IOException {
        return meetingDao.addPlaceList(mid, meetplace);
    }

    @Override
    public String addUserToMeeting(int mid, List<UsersDto> joiners) throws InterruptedException, ExecutionException, IOException {
        return meetingDao.addUserToMeeting(mid, joiners);
    }

    @Override
    public String deleteUserFromMeeting(int mid, UsersDto joiner) throws InterruptedException, ExecutionException {
        return meetingDao.deleteUserFromMeeting(mid, joiner);
    }

    @Override
    public String updateTime(int mid, Date time) throws InterruptedException, ExecutionException, IOException {
        return meetingDao.updateTime(mid, time);
    }

    @Override
    public String deleteMeeting(MeetEntity meetEntity) throws InterruptedException, ExecutionException {
        return meetingDao.deleteMeeting(meetEntity);
    }

}