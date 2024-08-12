package com.example.Foodle.dto.request.meeting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.example.Foodle.dto.request.meetingPlace.MeetingPlaceDto;
import com.example.Foodle.dto.request.place.PlaceDto;
import com.example.Foodle.dto.request.user.UsersDto;
import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.entity.MeetingPlaceEntity;
import com.example.Foodle.entity.MeetingPlaceInfoEntity;
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

    private List<UsersDto> joiners;

    private List<MeetingPlaceDto> places;
    
    // 기본 생성자
    public MeetingDto() {
        this.mid = -1;
        this.name = "";
        this.date = null;
        this.joiners = new ArrayList<>();
        this.places = null;
    }

    // 생성자
    public MeetingDto(int mid, String name, Date date, List<MeetingPlaceDto> places, List<UsersDto> joiners) {
        this.mid = mid;
        this.name = name;
        this.date = date;
        this.joiners = joiners != null ? joiners : new ArrayList<>();
        this.places = places != null ? places : new ArrayList<>();
    }
    

    public MeetEntity toEntity() {
        List<String> joinersIds = new ArrayList<>();
        for (UsersDto user : joiners) {
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
                    place.getLongtitude()
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
