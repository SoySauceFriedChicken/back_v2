package com.example.Foodle.entity;


import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Transient;

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
public class FriendEntity {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;
    
    @Column(length = 100, nullable = false)
    private Boolean like;
    private int fid;
    
    @Transient // 이 필드는 데이터베이스에 매핑되지 않습니다.
    private UsersEntity user;

    // Optional: fetch user data on demand
    @PostLoad
    public void fetchUser() {
        // Implement logic to fetch the UsersEntity based on fid
        // This will typically involve calling a service to get the user by fid
    }

}
