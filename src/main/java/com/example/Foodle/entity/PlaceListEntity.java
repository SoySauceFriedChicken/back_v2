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
public class PlaceListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int lid;
    
    @Column(length = 100, nullable = false)
    private int uid;
    private String name; // 이름
    private String color; // 색상

    @ElementCollection
    @CollectionTable(name = "placeList", joinColumns = @JoinColumn(name = "pid"))
    @Column(name = "Place_id")
    private List<Integer> pid;

    // @ElementCollection
    // @CollectionTable(name = "placeList", joinColumns = @JoinColumn(name = "mid"))
    // @Column(name = "place_id") // or any appropriate column name
    // private List<Integer> placeList;
}
