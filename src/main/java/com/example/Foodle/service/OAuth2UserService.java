// package com.example.Foodle.service;

// import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
// import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
// import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
// import org.springframework.security.oauth2.core.user.OAuth2User;
// import org.springframework.stereotype.Service;

// import com.fasterxml.jackson.databind.ObjectMapper;

// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;

// @Service
// @RequiredArgsConstructor
// @Slf4j
// public class OAuth2UserService extends DefaultOAuth2UserService{
//     @Override
//     public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
       
//         OAuth2User oAuth2User = super.loadUser(request); // Cast the result to OAuth2User
//         // String oauthClientName = request.getClientRegistration().getClientName(); // Get the client name

//         log.info("OAuth2UserServiceImplement.loadUser called");

//         try {
//             System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
//             log.info("OAuth2UserServiceImplement.loadUser: " + new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
//         } catch (Exception exception) {
//             log.error("Error logging user attributes", exception);
//             exception.printStackTrace();
//         }

//         // UsersEntity usersEntity = null;
//         // String userId = null;
//         // String name = null;
//         // String email = null;

//         // if (oauthClientName.equals("kakao")) {
//         //     userId = "kakao_" + oAuth2User.getAttributes().get("id");
//         //     name = oAuth2User.getAttributes().get("nickname").toString();
//         //     email = oAuth2User.getAttributes().get("kakao_account.email").toString();
//         //     usersEntity = new UsersEntity(userId, name, "새로운 사용자", "", "kakao", email);
//         // }
//         // if (oauthClientName.equals("naver")) {
//         //     Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttributes().get("response");
//         //     userId = "naver_" + ((String) response.get("id")).substring(0, 14);
//         //     email = response.get("email").toString();
//         //     name = response.get("name").toString();
//         //     usersEntity = new UsersEntity(userId, name, "새로운 사용자", "", "naver", email);

//         // }
//         // usersDao.saveUser(usersEntity);

//         return oAuth2User;
//     }
// }
