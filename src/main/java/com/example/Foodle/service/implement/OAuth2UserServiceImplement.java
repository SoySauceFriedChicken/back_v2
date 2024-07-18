package com.example.Foodle.service.implement;

import com.example.Foodle.dao.UsersDao;
import com.example.Foodle.entity.UsersEntity;
import com.example.Foodle.entity.customOAuth2User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserServiceImplement extends DefaultOAuth2UserService {
    private final UsersDao usersDao;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        try {
            OAuth2User oAuth2User = super.loadUser(request);
            String oauthClientName = request.getClientRegistration().getClientName();

            // log.info("OAuth2UserServiceImplement.loadUser: {}", new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
            
            // 사용자 정보 처리 로직 추가
            UsersEntity usersEntity = null;
            String userId = null;
            String name = null;
            String email = null;
            String profileImage = null;

             if (oauthClientName.equals("kakao")) {
                userId = "kakao_" + oAuth2User.getAttributes().get("id");
                Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
                if (kakaoAccount != null) {
                    Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                    name = profile != null ? (String) profile.get("nickname") : null;
                    email = (String) kakaoAccount.get("email");
                    profileImage = profile != null ? (String) profile.get("profile_image_url") : null;
                }
                usersEntity = new UsersEntity(userId, name, "새로운 사용자", profileImage, "kakao", email, "");
            } else if (oauthClientName.equals("naver")) {
                Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttributes().get("response");
                if (response != null) {
                    userId = "naver_" + ((String) response.get("id")).substring(0, 14);
                    email = (String) response.get("email");
                    name = (String) response.get("name");
                    profileImage = (String) response.get("profile_image");
                }
                usersEntity = new UsersEntity(userId, name, "새로운 사용자", profileImage, "naver", email, "");
            }

            usersDao.saveUser(usersEntity);
            
            return new customOAuth2User(userId);
        } catch (OAuth2AuthenticationException e) {
            // log.error("OAuth2AuthenticationException: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            // log.error("Exception: {}", e.getMessage(), e);
            throw new OAuth2AuthenticationException(null, e.getMessage(), e);
        }

        // UsersEntity usersEntity = null;
        // String userId = null;
        // String name = null;
        // String email = null;

        // if (oauthClientName.equals("kakao")) {
        //     userId = "kakao_" + oAuth2User.getAttributes().get("id");
        //     name = oAuth2User.getAttributes().get("nickname").toString();
        //     email = oAuth2User.getAttributes().get("kakao_account.email").toString();
        //     usersEntity = new UsersEntity(userId, name, "새로운 사용자", "", "kakao", email);
        // }
        // if (oauthClientName.equals("naver")) {
        //     Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttributes().get("response");
        //     userId = "naver_" + ((String) response.get("id")).substring(0, 14);
        //     email = response.get("email").toString();
        //     name = response.get("name").toString();
        //     usersEntity = new UsersEntity(userId, name, "새로운 사용자", "", "naver", email);

        // }
        // usersDao.saveUser(usersEntity);

        
    }
}
