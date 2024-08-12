package com.example.Foodle.dto.request.user;


import java.util.ArrayList;
import java.util.List;

import com.example.Foodle.entity.PreferredTimeEntity;
import com.example.Foodle.entity.UsersEntity;

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

    private List<PreferredTimeDto> preferredTime;
    private List<String> likeWord;
    private List<String> dislikeWord;

    public UsersEntity toEntity() {
        //PreferredTimeDto를 PreferredTimeEntity로 변환
        if(preferredTime == null) {
            return new UsersEntity(uid, name, nickName, profileImage, null, likeWord, dislikeWord);
        }

        List<PreferredTimeEntity> preferredTimeEntities = new ArrayList<>();
        PreferredTimeEntity preferredTimeEntity = new PreferredTimeEntity();
        for(PreferredTimeDto preferredTimeDto : preferredTime) {
            preferredTimeEntity.setDay(preferredTimeDto.getDay());
            preferredTimeEntity.setStart(preferredTimeDto.getStart().toString());
            preferredTimeEntity.setEnd(preferredTimeDto.getEnd().toString());
            preferredTimeEntities.add(preferredTimeEntity);
        }
        return new UsersEntity(uid, name, nickName, profileImage, preferredTimeEntities, likeWord, dislikeWord);
    }
}