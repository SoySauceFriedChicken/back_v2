package com.example.Foodle.dto.request.meetingPlace;

import com.example.Foodle.dto.request.place.PlaceDto;
import com.example.Foodle.entity.UsersEntity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MeetingPlaceDto {
    private PlaceDto place;
    private Date time;

    @JsonCreator
    public MeetingPlaceDto(
        @JsonProperty("place") PlaceDto place,
        @JsonProperty("time") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z", timezone = "UTC") Date time
        ) {
        this.place = place;
        this.time = time;
    }
}
