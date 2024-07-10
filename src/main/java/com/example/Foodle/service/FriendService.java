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

    public List<FriendEntity> getFriendsByUid(String uid) throws ExecutionException, InterruptedException {
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
    public List<FriendDto> getFriendsWithUserDetails(String uid) throws ExecutionException, InterruptedException {
        Firestore db = getFirestore();
        CollectionReference friendsRef = db.collection(COLLECTION_NAME);
        Query query = friendsRef.whereEqualTo("uid", uid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        List<FriendDto> friendsWithUserDetails = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            FriendEntity friendEntity = document.toObject(FriendEntity.class);
    
            log.info("User found: " + friendEntity.getUid());
                Query userRef = db.collection(USERS_COLLECTION_NAME).whereEqualTo("uid", friendEntity.getFid());
                
                ApiFuture<QuerySnapshot> userSnapshot = userRef.get();
                QuerySnapshot userDocument = userSnapshot.get();
                
                // Check if user document exists
                if (!userDocument.isEmpty()) {
                    log.info("User found: " + userDocument.getDocuments().get(0).toObject(UsersEntity.class));
                    UsersEntity userEntity = userDocument.getDocuments().get(0).toObject(UsersEntity.class);
                    friendEntity.setUser(userEntity);
                } else{
                    log.info("User not found");
                    
                }

            
            friendsWithUserDetails.add(friendEntity.toDto());
        }
        return friendsWithUserDetails;
    }
    // @Autowired
    // private FriendRepository friendRepository;

    public void createFriend(String uid, String fid) {
        Firestore db = getFirestore();
        // Create references for each friend relationship
        DocumentReference friendRef1 = db.collection(COLLECTION_NAME).document();
        DocumentReference friendRef2 = db.collection(COLLECTION_NAME).document();

        FriendEntity friendEntity1 = new FriendEntity(uid, false, fid);
        FriendEntity friendEntity2 = new FriendEntity(fid, false, uid);

        // Create two separate futures for each write operation
        ApiFuture<WriteResult> future1 = friendRef1.set(friendEntity1);
        ApiFuture<WriteResult> future2 = friendRef2.set(friendEntity2);

        try {
            // Wait for both operations to complete
            WriteResult result1 = future1.get();
            WriteResult result2 = future2.get();

            // Log the update times (optional)
            System.out.println("Update time for friendRef1: " + result1.getUpdateTime());
            System.out.println("Update time for friendRef2: " + result2.getUpdateTime());

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void updateFriend(String uid, String fid) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference friendRef = db.collection(COLLECTION_NAME)
                .whereEqualTo("uid", uid)
                .whereEqualTo("fid", fid)
                .limit(1)  // Limit to one document (assuming uid, fid combination is unique)
                .get()
                .get()
                .getDocuments()
                .get(0).getReference();

        // Fetch the current value of 'like' field
        ApiFuture<DocumentSnapshot> future = friendRef.get();
        DocumentSnapshot document = future.get();
        boolean currentLikeValue = document.getBoolean("like");

        // Update the 'like' field with its negation
        friendRef.update("like", !currentLikeValue);
    }

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