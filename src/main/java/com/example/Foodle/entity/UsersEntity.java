package com.example.Foodle.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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
    private String uid;
    private String name;
    private String nickName;
    private String profileImage;
    // private String role;

    public UsersEntity(String uid, String name, String nickName, String profileImage) {
        this.uid = uid;
        this.name = name;
        this.nickName = nickName;
        this.profileImage = profileImage;
        // this.role = role;
    }
}