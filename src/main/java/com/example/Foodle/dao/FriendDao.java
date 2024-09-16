package com.example.Foodle.dao;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Repository;

import com.example.Foodle.dto.request.friend.FriendDto;
import com.example.Foodle.dto.request.user.PreferredTimeDto;
import com.example.Foodle.dto.request.user.UsersDto;
import com.example.Foodle.entity.FriendEntity;
import com.example.Foodle.entity.MeetEntity;
import com.example.Foodle.entity.PreferredTimeEntity;
import com.example.Foodle.entity.UsersEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
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

    public static LocalTime convertTimeStringToLocalTime(String time) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(time, timeFormatter);
    }

    public String createFriendByCode(String uid, String code) throws ExecutionException, InterruptedException {
    Firestore db = getFirestore();

    // Query to find the user with the given friend code
    CollectionReference UsersRef = db.collection(USERS_COLLECTION_NAME);
    Query usersQuery = UsersRef.whereEqualTo("friendCode", code);
    ApiFuture<QuerySnapshot> usersQuerySnapshot = usersQuery.get();
    List<QueryDocumentSnapshot> usersDocuments = usersQuerySnapshot.get().getDocuments();
    
    // Check if any user was found with the provided friend code
    if (usersDocuments.isEmpty()) {
        return "User not found with the provided friend code";
    }

    // At this point, we know the list is not empty
    UsersEntity user = usersDocuments.get(0).toObject(UsersEntity.class);

    // Check if the friendship already exists
    Query querySnapshot = db.collection(COLLECTION_NAME)
        .whereEqualTo("uid", uid)
        .whereEqualTo("fid", user.getUid());
    ApiFuture<QuerySnapshot> query = querySnapshot.get();
    List<QueryDocumentSnapshot> documents = query.get().getDocuments();
    if (!documents.isEmpty()) {
        return "Friend already exists";
    }

    // Create new friend documents if they are not already friends
    DocumentReference friendRef1 = db.collection(COLLECTION_NAME).document();
    DocumentReference friendRef2 = db.collection(COLLECTION_NAME).document();

    UsersDao usersDao = new UsersDao();
    FriendEntity friendEntity1 = new FriendEntity(uid, false, user.getUid());
    FriendEntity friendEntity2 = new FriendEntity(user.getUid(), false, uid);

    // Write the friend entities to the Firestore
    ApiFuture<WriteResult> future1 = friendRef1.set(friendEntity1);
    ApiFuture<WriteResult> future2 = friendRef2.set(friendEntity2);

    try {
        // Wait for both operations to complete
        WriteResult result1 = future1.get();
        WriteResult result2 = future2.get();

        // Log the update times (optional)
        System.out.println("Update time for friendRef1: " + result1.getUpdateTime());
        System.out.println("Update time for friendRef2: " + result2.getUpdateTime());

        return "Friend added Successfully";

    } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
        return "Error while adding friend";
    }
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
                    UsersEntity usersEntity = userDocument.getDocuments().get(0).toObject(UsersEntity.class);

                    UsersDto userDto = new UsersDto(usersEntity.getUid(), usersEntity.getName(), usersEntity.getNickName(), usersEntity.getProfileImage(), null, usersEntity.getLikeWord(), usersEntity.getDislikeWord());
                    List<PreferredTimeDto> preferredTimeList = new ArrayList<>();
                    
                    if(usersEntity.getPreferredTime() != null) {
                        for(PreferredTimeEntity preferredTime : usersEntity.getPreferredTime()) {
                            PreferredTimeDto preferredTimeDto = new PreferredTimeDto();
                            preferredTimeDto.setDay(preferredTime.getDay());
                            preferredTimeDto.setStart(preferredTime.getStart());
                            preferredTimeDto.setEnd(preferredTime.getEnd());
                            preferredTimeList.add(preferredTimeDto);
                        }
                        userDto.setPreferredTime(preferredTimeList);
                    }               

                    friendDto.setUser(userDto);
                    friendDto.setLike(friendEntity.getLike());
                } else{
                    log.info("User not found");
                    
                }
            friendsWithUserDetails.add(friendDto);
        }
        friendsWithUserDetails.sort((f1, f2) -> f1.getUser().getUid().compareTo(f2.getUser().getUid()));
        return friendsWithUserDetails;
    }

    // 친구 삭제
    public String deleteAllFriend(String uid) throws InterruptedException, ExecutionException {
        Firestore db = getFirestore();
        CollectionReference friendsRef = db.collection(COLLECTION_NAME);
        Query query = friendsRef.whereEqualTo("uid", uid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        Query query2 = friendsRef.whereEqualTo("fid", uid);
        ApiFuture<QuerySnapshot> querySnapshot2 = query2.get();
        List<QueryDocumentSnapshot> documents2 = querySnapshot2.get().getDocuments();

        // try catch로 감싸기
        try{
            if(documents.isEmpty()) {
                return "No friensds found";
            }
            for (QueryDocumentSnapshot document : documents) {
                document.getReference().delete();
            }
            for (QueryDocumentSnapshot document : documents2) {
                document.getReference().delete();
            }
            return "Friend deleted successfully";
        } catch (Exception e) {
            return "Error while deleting friends";
        }
    }
}
