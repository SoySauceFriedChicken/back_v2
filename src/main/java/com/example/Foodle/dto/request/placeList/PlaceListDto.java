package com.example.Foodle.dto.request.placeList;

import java.util.List;

import com.example.Foodle.dto.request.place.PlaceDto;
import com.example.Foodle.entity.PlaceEntity;
import java.awt.Color;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlaceListDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int lid;
    
    @Column(length = 100, nullable = false)
    private String name; // 이름
    private String color; // 색상

    private List<PlaceDto> places;
}
