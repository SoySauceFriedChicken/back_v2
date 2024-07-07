package com.example.Foodle.entity;



import java.sql.Time;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class MeetingPlaceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pid;
    private Time time;
    
}