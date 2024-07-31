package com.example.Foodle.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Repository;

import com.example.Foodle.dto.request.friend.FriendDto;
import com.example.Foodle.dto.request.user.UsersDto;
import com.example.Foodle.entity.FriendEntity;
import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.entity.UsersEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import lombok.extern.slf4j.Slf4j;


@Repository
@Slf4j
public class FriendDao {
    public static final String COLLECTION_NAME = "Friend";
    public static final String USERS_COLLECTION_NAME = "Users";

    private Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }

    public List<FriendDto> getFriendsByUid(String uid) throws ExecutionException, InterruptedException {
        Firestore db = getFirestore();
        CollectionReference friendsRef = db.collection(COLLECTION_NAME);
        Query query = friendsRef.whereEqualTo("uid", uid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        List<FriendDto> friendsWithUserDetails = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            FriendEntity friendEntity = document.toObject(FriendEntity.class);
            FriendDto friendDto = new FriendDto();
    
            log.info("User found: " + friendEntity.getUid());
                Query userRef = db.collection(USERS_COLLECTION_NAME).whereEqualTo("uid", friendEntity.getFid());
                
                ApiFuture<QuerySnapshot> userSnapshot = userRef.get();
                QuerySnapshot userDocument = userSnapshot.get();
                
                // Check if user document exists
                if (!userDocument.isEmpty()) {
                    // log.info("User found: " + userDocument.getDocuments().get(0).toObject(UsersEntity.class));
                    UsersDto usersDto = userDocument.getDocuments().get(0).toObject(UsersDto.class);
                    friendDto.setUser(usersDto);
                    friendDto.setLike(friendEntity.getLike());
                } else{
                    log.info("User not found");
                    
                }
            friendsWithUserDetails.add(friendDto);
        }
        friendsWithUserDetails.sort((f1, f2) -> f1.getUser().getUid().compareTo(f2.getUser().getUid()));
        return friendsWithUserDetails;
    }
}
