package com.example.Foodle.dto.request.place;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List; // Import the correct List class
import java.util.ArrayList;  // ArrayList 임포트

@Getter
@Setter
@AllArgsConstructor
public class PlaceDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 100, nullable = false)
    private String placeName; // 이름
    private String tel; // 전화번호

    private List<String> images; // 이미지
    private String instaURL; // 인스타그램 URL
    private Double rating; // 평점
    private String reviewURL; // 리뷰 URL
    private String category; // 카테고리 
    private String address; // 주소

    private double latitude;
    private double longtitude;
    
    private List<String> breakTime;

    private List<String> working;

    public PlaceDto(String name, double latitude, double longtitude){
        this.placeName = name;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.tel = "";
        this.images = new ArrayList<>();
        this.instaURL = "";
        this.rating = 0.0;
        this.reviewURL = "";
        this.category = "";
        this.address = "";
        this.breakTime = new ArrayList<>();
        this.working = new ArrayList<>();
    }

    public PlaceDto(){
        this.placeName = "";
        this.latitude = 0;
        this.longtitude = 0;
        this.tel = "";
        this.images = new ArrayList<>();
        this.instaURL = "";
        this.rating = 0.0;
        this.reviewURL = "";
        this.category = "";
        this.address = "";
        this.breakTime = new ArrayList<>();
        this.working = new ArrayList<>();
    }
}
