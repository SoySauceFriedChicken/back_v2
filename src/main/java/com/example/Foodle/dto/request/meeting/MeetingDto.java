package com.example.Foodle.dto.request.meeting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.example.Foodle.entity.UsersEntity;

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
    private final int uid;
    private final String name;
    private final Date date;

    private final List<UsersEntity> joiners;

    private final List<Map<String, Object>> placeList;
    
    // 기본 생성자
    public MeetingDto() {
        this.mid = 0;
        this.uid = 0;
        this.name = "";
        this.date = null;
        this.joiners = new ArrayList<>();
        this.placeList = null;
    }

    // 생성자
    public MeetingDto(int mid, int uid, String name, Date date, List<Map<String, Object>> placeList, List<UsersEntity> joiners) {
        this.mid = mid;
        this.uid = uid;
        this.name = name;
        this.date = date;
        this.joiners = joiners != null ? joiners : new ArrayList<>();
        this.placeList = placeList != null ? placeList : new ArrayList<>();
    }

    
}
