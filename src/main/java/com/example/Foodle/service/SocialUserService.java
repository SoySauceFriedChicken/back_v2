// package com.example.Foodle.service;

// import com.example.Foodle.entity.SocialUserEntity;
// import com.example.Foodle.repository.SocialUserRepository;
// import com.example.Foodle.repository.UserRepository;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;

// import java.util.Optional;

// @Service
// @RequiredArgsConstructor
// public class SocialUserService {

//     private final SocialUserRepository socialUserRepository;

//     public Optional<SocialUserEntity> getUserBySnsIdAndProvider(String snsId, String provider) {
//         return socialUserRepository.findBySnsIdAndProvider(snsId, provider);
//     }

//     public Optional<SocialUserEntity> getUserByEmail(String email) {
//         return socialUserRepository.findByEmail(email);
//     }

//     public SocialUserEntity saveUser(SocialUserEntity user) {
//         return socialUserRepository.save(user);
//     }
// }
