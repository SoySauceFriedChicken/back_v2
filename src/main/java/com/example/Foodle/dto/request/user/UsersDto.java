package com.example.Foodle.dto.request.user;


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
public class UsersDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String uid;
    private String name;
    private String nickName;
    private String profileImage;

    
}