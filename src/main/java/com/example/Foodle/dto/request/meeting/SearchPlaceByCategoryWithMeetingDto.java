package com.example.Foodle.dto.request.meeting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchPlaceByCategoryWithMeetingDto {
    MeetingDto meeting;
    String category;
}
