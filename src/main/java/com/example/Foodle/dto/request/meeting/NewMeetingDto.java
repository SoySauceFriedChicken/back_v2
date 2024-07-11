package com.example.Foodle.dto.request.meeting;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;

import com.example.Foodle.dto.request.meetingPlace.MeetingPlaceDto;
import com.example.Foodle.dto.request.place.PlaceDto;
import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.entity.MeetingPlaceEntity;
import com.example.Foodle.entity.MeetingPlaceInfoEntity;
import com.example.Foodle.entity.UsersEntity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.SimpleDateFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@ToString
//@AllArgsConstructor
@Slf4j
public class NewMeetingDto {
    private final int mid;
    private final String name;
    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z", timezone = "UTC")
    private Date date;

    private final List<UsersEntity> joiners;
    private final List<MeetingPlaceDto> places;

    @JsonCreator
    public NewMeetingDto(
        @JsonProperty("mid") int mid,
        @JsonProperty("uid") String uid,
        @JsonProperty("name") String name,
        @JsonProperty("date") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z", timezone = "UTC") Date date,
        @JsonProperty("joiners") List<UsersEntity> joiners,
        @JsonProperty("places") List<MeetingPlaceDto> places) {
        this.mid = mid;
        this.name = name;
        this.date = date;
        this.joiners = joiners;
        this.places = places;
    }


    public MeetEntity toEntity() {
        List<String> joinersIds = new ArrayList<>();
        for (UsersEntity user : joiners) {
            joinersIds.add(user.getUid());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
        List<MeetingPlaceEntity> lists = new ArrayList<>();
        if (places != null) {
            for (MeetingPlaceDto placeEntry : places) {
                PlaceDto place = (PlaceDto) placeEntry.getPlace();
                MeetingPlaceEntity newPlace = new MeetingPlaceEntity();

                MeetingPlaceInfoEntity meetingPlaceInfoEntity = new MeetingPlaceInfoEntity(
                    place.getPlaceName(),
                    place.getLatitude(),
                    place.getLongitude()
                );
                newPlace.setPlace(meetingPlaceInfoEntity);
                // log.info("place.get(\"pid\") : " + place.get("pid"));

                // placeEntry.get("time")이 Date 객체일 경우
                if (placeEntry.getTime() instanceof Date) {
                    Date date = (Date) placeEntry.getTime();
                    ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.of("UTC"));
                    newPlace.setTime(formatter.format(zonedDateTime));
                }
                lists.add(newPlace);
            }
        }

       // Date 객체를 String으로 변환
        String formattedDate = formatter.format(ZonedDateTime.ofInstant(date.toInstant(), ZoneId.of("UTC")));
        return new MeetEntity(mid, name, formattedDate, joinersIds, lists);
    }
}
