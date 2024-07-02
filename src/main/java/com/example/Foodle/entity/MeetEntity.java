package com.example.Foodle.entity;

import java.util.Date;
import java.util.List;

import com.google.auto.value.AutoValue.Builder;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import jakarta.persistence.JoinColumn;

@Entity
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mid;
    private int uid;
    
    @Column(length = 100, nullable = false)
    private String name;
    private Date date;
    // private String member;
    // private String placeList;

    @ElementCollection
    @CollectionTable(name = "member", joinColumns = @JoinColumn(name = "mid"))
    @Column(name = "member_id") // or any appropriate column name
    private List<Integer> member;

    @ElementCollection
    @CollectionTable(name = "placeList", joinColumns = @JoinColumn(name = "mid"))
    @Column(name = "place_id") // or any appropriate column name
    private List<Integer> placeList;
}
