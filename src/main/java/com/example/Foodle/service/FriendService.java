package com.example.Foodle.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Foodle.dao.FriendDao;
import com.example.Foodle.dao.UsersDao;
import com.example.Foodle.dto.request.friend.FriendDto;
import com.example.Foodle.entity.FriendEntity;
import com.example.Foodle.entity.UsersEntity;
// import com.example.Foodle.repository.FriendRepository;
// import com.example.Foodle.repository.UsersRepository;
import com.google.api.core.ApiFuture;
import com.google.firebase.cloud.FirestoreClient;

import lombok.extern.slf4j.Slf4j;

import com.google.cloud.firestore.*;

@Service
@Slf4j
public class FriendService {

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private UsersDao usersDao;

    private static final String COLLECTION_NAME = "Friend";
    private static final String USERS_COLLECTION_NAME = "Users";
    

    private Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }

    public List<FriendEntity> getFriendsByUid(int uid) throws ExecutionException, InterruptedException {
        Firestore db = getFirestore();
        CollectionReference friends = db.collection(COLLECTION_NAME);
        Query query = friends.whereEqualTo("uid", uid); // Use the correct Query class
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        
        List<FriendEntity> friendsByName = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            friendsByName.add(document.toObject(FriendEntity.class));
        }
        return friendsByName;
    }

    // 새로운 메서드
    public List<FriendDto> getFriendsWithUserDetails(int uid) throws ExecutionException, InterruptedException {
        Firestore db = getFirestore();
        CollectionReference friendsRef = db.collection(COLLECTION_NAME);
        Query query = friendsRef.whereEqualTo("uid", uid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        List<FriendDto> friendsWithUserDetails = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            FriendDto friendDto = document.toObject(FriendDto.class);
    
            log.info("User found: " + friendDto.getUser());
                Query userRef = db.collection(USERS_COLLECTION_NAME).whereEqualTo("uid", friendDto.getFid());
                
                ApiFuture<QuerySnapshot> userSnapshot = userRef.get();
                QuerySnapshot userDocument = userSnapshot.get();
                
                // Check if user document exists
                if (!userDocument.isEmpty()) {
                    log.info("User found: " + userDocument.getDocuments().get(0).toObject(UsersEntity.class));
                    UsersEntity userEntity = userDocument.getDocuments().get(0).toObject(UsersEntity.class);
                    friendDto.setUser(userEntity);
                } else{
                    log.info("User not found");
                    
                }

            
            friendsWithUserDetails.add(friendDto);
        }
        return friendsWithUserDetails;
    }
    // @Autowired
    // private FriendRepository friendRepository;

    // @Autowired
    // private UsersRepository usersRepository;

    // public FriendEntity getFriendWithUser(int uid) {
    //     Optional<FriendEntity> friendOpt = friendRepository.findById(uid);
    //     if (friendOpt.isPresent()) {
    //         FriendEntity friend = friendOpt.get();
    //         Optional<UsersEntity> userOpt = usersRepository.findById(friend.getFid());
    //         userOpt.ifPresent(friend::setUser);
    //         return friend;
    //     }
    //     return null;
    // }
}