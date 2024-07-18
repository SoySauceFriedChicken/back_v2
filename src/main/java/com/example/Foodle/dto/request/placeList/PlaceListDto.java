package com.example.Foodle.dto.request.placeList;

import java.util.List;

import com.example.Foodle.dto.request.place.PlaceDto;
import com.example.Foodle.entity.MeetingPlaceInfoEntity;
import com.example.Foodle.entity.PlaceEntity;
import com.example.Foodle.entity.PlaceListEntity;

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
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceListDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int lid;
    
    @Column(length = 100, nullable = false)
    private String uid;
    private String name; // 이름
    private String color; // 색상

    private List<PlaceDto> places;

    public PlaceListEntity toEntity(){
        List<MeetingPlaceInfoEntity> meetingPlaceInfoEntity = new ArrayList<>();
        for(PlaceDto placeDto : places){
            meetingPlaceInfoEntity.add(new MeetingPlaceInfoEntity(placeDto.getPlaceName(), placeDto.getLatitude(), placeDto.getLongtitude()));
        }
        return new PlaceListEntity(lid, uid, name, color, meetingPlaceInfoEntity);
    }
}
