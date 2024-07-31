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

    // 새로운 메서드
    public List<FriendDto> getFriendsWithUserDetails(String uid) throws ExecutionException, InterruptedException {
        return friendDao.getFriendsByUid(uid);
    }
    // @Autowired
    // private FriendRepository friendRepository;

    public void createFriend(String uid, String fid) throws InterruptedException, ExecutionException {
        Firestore db = getFirestore();

        // 이미 친구로 등록되어 있는지 확인
        DocumentReference friendRef1 = db.collection(COLLECTION_NAME)
                .whereEqualTo("uid", uid)
                .whereEqualTo("fid", fid)
                .limit(1)  // Limit to one document (assuming uid, fid combination is unique)
                .get()
                .get()
                .getDocuments()
                .get(0).getReference();
        
        // 이미 친구로 등록되어 있는 경우
        if (friendRef1 != null) {
            return;
        }
        DocumentReference friendRef2 = db.collection(COLLECTION_NAME).document();

        UsersDao usersDao = new UsersDao();

        FriendDto friendEntity1 = new FriendDto(usersDao.findByUid(uid), false);
        FriendDto friendEntity2 = new FriendDto(usersDao.findByUid(fid), false);

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