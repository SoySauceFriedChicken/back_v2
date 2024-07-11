package com.example.Foodle.entity;



import java.sql.Time;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MeetingPlaceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private MeetingPlaceInfoEntity place;
    private String time;
    
}