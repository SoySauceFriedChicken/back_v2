// package com.example.Foodle.service.implement;

// import com.example.Foodle.entity.UserEntity;
// import com.example.Foodle.entity.customOAuth2User;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;

// import lombok.RequiredArgsConstructor;

// import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
// import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
// import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
// import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
// import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
// import org.springframework.security.oauth2.core.user.OAuth2User;
// import org.springframework.stereotype.Service;
    
// import com.example.Foodle.repository.UserRepository;

// import java.util.Map;

// @Service
// @RequiredArgsConstructor
// public class OAuth2UserServiceImplement extends DefaultOAuth2UserService {
//     private final UserRepository userRepository;
//     @Override
//     public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
//         OAuth2User oAuth2User = super.loadUser(request); // Cast the result to OAuth2User
//         String oauthClientName = request.getClientRegistration().getClientName(); // Get the client name

//         try{
//             System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
//         } catch (Exception exception) {
//             exception.printStackTrace();
//         }

//         UserEntity userEntity = null;
//         String userId = null;
//         String email = null;

//         if (oauthClientName.equals("kakao")) {
//             userId = "kakao_" + oAuth2User.getAttributes().get("id");
//             userEntity = new UserEntity(userId, "email@email.com", "kakao");
//         }
//         if (oauthClientName.equals("naver")) {
//             Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttributes().get("response");
//             userId = "naver_" + ((String) response.get("id")).substring(0, 14);
//             email = response.get("email").toString();
//             userEntity = new UserEntity(userId, email, "naver");

//         }
//         userRepository.save(userEntity);

//         return oAuth2User;
//     }
// }
