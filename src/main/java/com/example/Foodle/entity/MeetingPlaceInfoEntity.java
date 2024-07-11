package com.example.Foodle.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MeetingPlaceInfoEntity {
    private String placeName;
    private double latitude;
    private double longitude;
}
