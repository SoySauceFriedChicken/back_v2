package com.example.Foodle.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class UsersEntity {
    private int uid;
    private String name;
    @ElementCollection
    @CollectionTable(name = "placeList", joinColumns = @JoinColumn(name = "pid"))
    @Column(name = "Place_id")
    private List<Integer> placeList;
    private String icon;
}