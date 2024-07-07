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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;
    private String name;
    private String nickName;
    @ElementCollection
    @CollectionTable(name = "placeList", joinColumns = @JoinColumn(name = "pid"))
    @Column(name = "Place_id")
    private List<Integer> placeList;
    private String profileImage;

    public UsersEntity(int uid, String name, String nickName, String profileImage) {
        this.uid = uid;
        this.name = name;
        this.nickName = nickName;
        this.profileImage = profileImage;
    }

    @Override
    public String toString() {
        return "UsersEntity{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", nickName='" + nickName + '\'' +
                ", placeList=" + placeList +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }
}