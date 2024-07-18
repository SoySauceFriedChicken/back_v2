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

// import org.springframework.data.geo.*;
// import org.springframework.core.convert.converter.Converter;
// import org.springframework.data.convert.WritingConverter;
// import org.springframework.data.convert.ReadingConverter;

@Entity
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceEntity {
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

    // @ElementCollection
    // @CollectionTable(name = "coordinates", joinColumns = @JoinColumn(name = "location_id"))
    // @Column(name = "address")
    // private List<Double> address = new ArrayList<>();
    // @ElementCollection
    // @CollectionTable(name = "address", joinColumns = @JoinColumn(name = "pid"))
    // @MapKeyColumn(name = "type")
    // @Column(name = "value")
    // private Map<String, String> address = new HashMap<>();

    private double latitude;
    private double longtitude;

    // static <INSTANCE> List<Converter<?, ?>> getConvertersToRegister() {
    //     return [
    //         GeoJSONDBObjectToPointConverter.INSTANCE,
    //         GeoJSONDBObjectToPolygonConverter.INSTANCE,
    //         GeoJSONPointToDBObjectConverter.INSTANCE,
    //         GeoJSONPolygonToDBObjectConverter.INSTANCE
    //     ]
    // }

    
    @ElementCollection
    @CollectionTable(name = "breaktime_mapping", joinColumns = @JoinColumn(name = "pid"))
    @MapKeyColumn(name = "day_of_week")
    @Column(name = "breaktime")
    private List<String> breaktime = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "working_hours_mapping", joinColumns = @JoinColumn(name = "pid"))
    @MapKeyColumn(name = "day_of_week")
    @Column(name = "working_hours")
    private List<String> working = new ArrayList<>();

}
