package com.example.Foodle.dto.request.meeting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchPlaceByPlaceNameWithMeetingDto {
    MeetingDto meeting;
    String placeName;
}
