package com.example.Foodle.dto.request.meeting;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.entity.UsersEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UpdateMeetingDto {
    private final String mid;
    private final String uid;
    private final String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z", timezone = "UTC")
    private Date date;

    private final List<UsersEntity> joiners;
    private final List<Map<String, Object>> placeList;

    public MeetEntity toEntity() {
        List<String> joinersIds = new ArrayList<>();
        for (UsersEntity user : joiners) {
            joinersIds.add(user.getUid());
        }

        List<Map<String, Object>> lists = new ArrayList<>();
        if (placeList != null) {
            for (Map<String, Object> placeEntry : placeList) {
                Map<String, Object> place = (Map<String, Object>) placeEntry.get("place");
                Map<String, Object> newPlace = new HashMap<>();
                newPlace.put("place", place.get("pid"));
                // log.info("place.get(\"pid\") : " + place.get("pid"));
                newPlace.put("time", placeEntry.get("time").toString());
                lists.add(newPlace);
            }
        }

        String newDate = date.toString();
        return new MeetEntity(mid, uid, name, newDate, joinersIds, lists);
    }
}
