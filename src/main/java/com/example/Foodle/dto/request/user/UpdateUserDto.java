package com.example.Foodle.dto.request.user;

import com.example.Foodle.entity.UsersEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {
    private String uid;
    private String name;
    private String nickName;
    private String profileImage;

    public UsersEntity toEntity() {
        return new UsersEntity(uid, name, nickName, profileImage);
    }
}
