package com.example.Foodle.dto.request.meeting;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UpdateMeeingTimeDto {
    private int mid;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss Z")  // 날짜 형식을 맞추기 위해 사용
    private Date time;

}
