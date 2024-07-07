package com.example.Foodle.dto.request.user;

import com.example.Foodle.entity.UsersEntity;

import jakarta.annotation.Generated;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NewUserDto {
    
    @NotNull
    private Integer uid;

    @NotBlank
    private String name;

    @NotBlank
    private String nickName;

    @NotBlank
    private String profileImage;


    @Override
    public String toString() {
        return "NewUserDto{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", nickName='" + nickName + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }


    public UsersEntity toEntity() {
        return new UsersEntity(uid, name, nickName, profileImage);
    }
}
