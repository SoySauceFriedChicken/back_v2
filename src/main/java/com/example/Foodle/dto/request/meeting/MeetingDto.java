package com.example.Foodle.dto.request.meeting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.example.Foodle.dto.request.meetingPlace.MeetingPlaceDto;
import com.example.Foodle.entity.UsersEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
public class MeetingDto {
    private final int mid;
    private final String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z", timezone = "UTC")
    private Date date;

    private final List<UsersEntity> joiners;

    private final List<MeetingPlaceDto> places;
    
    // 기본 생성자
    public MeetingDto() {
        this.mid = -1;
        this.name = "";
        this.date = null;
        this.joiners = new ArrayList<>();
        this.places = null;
    }

    // 생성자
    public MeetingDto(int mid, String name, Date date, List<MeetingPlaceDto> places, List<UsersEntity> joiners) {
        this.mid = mid;
        this.name = name;
        this.date = date;
        this.joiners = joiners != null ? joiners : new ArrayList<>();
        this.places = places != null ? places : new ArrayList<>();
    }
    
}
