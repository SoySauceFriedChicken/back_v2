package com.example.Foodle.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.auto.value.AutoValue.Builder;
import com.google.protobuf.Struct;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pid;
    
    @Column(length = 100, nullable = false)
    private String name; // 이름
    private String tell; // 전화번호

    private String image; // 이미지
    private String instaUrl; // 인스타그램 URL
    private Double rating; // 평점
    private String reviewUrl; // 리뷰 URL
    private String category; // 주소

    // @ElementCollection
    // @CollectionTable(name = "coordinates", joinColumns = @JoinColumn(name = "location_id"))
    // @Column(name = "address")
    // private List<Double> address = new ArrayList<>();
    private double latitude;
    private double longitude;

    
    @ElementCollection
    @CollectionTable(name = "breaktime_mapping", joinColumns = @JoinColumn(name = "pid"))
    @MapKeyColumn(name = "day_of_week")
    @Column(name = "breaktime")
    private Map<String, String> breaktime = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "working_hours_mapping", joinColumns = @JoinColumn(name = "pid"))
    @MapKeyColumn(name = "day_of_week")
    @Column(name = "working_hours")
    private Map<String, String> working = new HashMap<>();

}
