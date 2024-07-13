package com.example.Foodle.dto.request.meetingPlace;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MeetingPlaceInfoDto {
    private String name;
    private double latitude;
    private double longitude;
}
