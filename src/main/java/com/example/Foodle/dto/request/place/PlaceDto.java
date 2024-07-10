package com.example.Foodle.dto.request.place;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String pid;
    
    @Column(length = 100, nullable = false)
    private String name; // 이름
    private String tell; // 전화번호

    private String image; // 이미지
    private String instaUrl; // 인스타그램 URL
    private Double rating; // 평점
    private String reviewUrl; // 리뷰 URL
    private String category; // 주소

    private double latitude;
    private double longitude;
    
    private Map<String, String> breaktime = new HashMap<>();

    private Map<String, String> working = new HashMap<>();

    public PlaceDto(String pid, String name, double latitude, double longitude){
        this.pid = pid;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
