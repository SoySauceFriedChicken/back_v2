package com.example.Foodle.entity;

import java.util.List;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
@AllArgsConstructor
public class UsersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String uid;
    private String name;
    private String nickName;
    private String profileImage;
    private String type;
    private String email;
    private String role;
    private List<PreferredTimeEntity> preferredTime;
    private List<String> likeWord;
    private List<String> dislikeWord;

    public UsersEntity(String uid, String name, String nickName, String profileImage, List<PreferredTimeEntity> preferredTime, List<String> likeWord, List<String> dislikeWord) {
        this.uid = uid;
        this.name = name;
        this.nickName = nickName;
        this.profileImage = profileImage;
        this.type = "";
        this.email = "";
        this.role = "USER";
        this.preferredTime = preferredTime;
        this.likeWord = likeWord;
        this.dislikeWord = dislikeWord;
    }
}