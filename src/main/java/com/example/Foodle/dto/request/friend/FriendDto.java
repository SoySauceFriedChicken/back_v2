package com.example.Foodle.dto.request.friend;

import com.example.Foodle.dto.request.user.UsersDto;
import com.example.Foodle.entity.FriendEntity;
import com.example.Foodle.entity.UsersEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendDto {
    private UsersDto user;
    private Boolean like;

}

