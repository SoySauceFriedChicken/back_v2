package com.example.Foodle.service.implement;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.example.Foodle.dao.MeetingDao;
import com.example.Foodle.dto.request.meeting.MeetingDto;
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
    public List<MeetingDto> getMeetingsByUid(int uid) throws ExecutionException, InterruptedException {
        return meetingDao.getMeetingsByUid(uid);
    }

    @Override // Add the @Override annotation
    public List<MeetingDto> getMeetingsByMid(int mid) throws ExecutionException, InterruptedException {
        return meetingDao.getMeetingsByMid(mid);
    }
}