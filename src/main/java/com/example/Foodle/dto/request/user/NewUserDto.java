package com.example.Foodle.dto.request.user;

import com.example.Foodle.entity.UsersEntity;

import jakarta.annotation.Generated;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewUserDto {
    @NotNull
    private String uid;
    private String name;
    private String nickName;
    private String profileImage;

    // public UsersEntity toEntity() {
    //     return new UsersEntity(uid, name, nickName, profileImage);
    // }
}
