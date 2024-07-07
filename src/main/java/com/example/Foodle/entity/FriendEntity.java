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

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

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
    private DocumentReference userReference;

    @Transient
    private UsersEntity user;

    public void setUserReference(DocumentReference userReference) {
        this.userReference = userReference;
    }

    public DocumentReference getUserReference() {
        return userReference;
    }

    public UsersEntity getUser() {
        if (user == null && userReference != null) {
            // Firestore에서 user 데이터를 가져옵니다.
            Firestore db = FirestoreClient.getFirestore();
            try {
                user = userReference.get().get().toObject(UsersEntity.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    public void setUser(UsersEntity user) {
        this.user = user;
    }


}
