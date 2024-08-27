package com.example.Foodle.dto.request.user;

import java.util.Date;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PreferredTimeDto {
    private String day;
    private String start;
    private String end;
}
