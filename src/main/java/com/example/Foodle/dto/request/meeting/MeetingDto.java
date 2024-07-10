package com.example.Foodle.dto.request.meeting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private final String mid;
    private final String uid;
    private final String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z", timezone = "UTC")
    private Date date;

    private final List<UsersEntity> joiners;

    private final List<Map<String, Object>> places;
    
    // 기본 생성자
    public MeetingDto() {
        this.mid = "";
        this.uid = "";
        this.name = "";
        this.date = null;
        this.joiners = new ArrayList<>();
        this.places = null;
    }

    // 생성자
    public MeetingDto(String mid, String uid, String name, Date date, List<Map<String, Object>> places, List<UsersEntity> joiners) {
        this.mid = mid;
        this.uid = uid;
        this.name = name;
        this.date = date;
        this.joiners = joiners != null ? joiners : new ArrayList<>();
        this.places = places != null ? places : new ArrayList<>();
    }

    // placeList를 업데이트하는 메서드
    public void updatePlaceList(Map<String, Object> meetplace) {
        Map<String, Object> newPlace = (Map<String, Object>) meetplace.get("place");
        Object newPid = newPlace.get("pid");

        boolean updated = false;

        for (Map<String, Object> existingEntry : places) {
            Map<String, Object> existingPlace = (Map<String, Object>) existingEntry.get("place");
            Object existingPid = existingPlace.get("pid");

            if (newPid.equals(existingPid)) {
                // 기존 엔트리를 업데이트
                existingPlace.putAll(newPlace);
                existingEntry.put("time", meetplace.get("time"));
                updated = true;
                break;
            }
        }

        if (!updated) {
            // 동일한 pid를 가진 엔트리가 없으면 새로 추가
            places.add(meetplace);
        }
    }
    
}
