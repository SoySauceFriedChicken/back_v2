package com.example.Foodle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Foodle.dto.request.user.UsersDto;
import com.example.Foodle.provider.JwtProvider;
import com.example.Foodle.service.UsersService;

import org.springframework.http.HttpStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import lombok.NoArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/api/v1")
public class OAuthController {
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String naverRedirectUri;

    

    /*
     * 카카오 로그인 요청
     * @return
     */
    @GetMapping("/kakao")
    public String kakaoConnect() throws UnsupportedEncodingException {
        StringBuilder url = new StringBuilder();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id=").append(kakaoClientId);
        url.append("&redirect_uri=").append(URLEncoder.encode(kakaoRedirectUri, "UTF-8"));
        url.append("&response_type=code");
        return "redirect:" + url.toString();
    }

    // 네이버 로그인 요청
    @GetMapping("/naver")
    public String naverConnect() throws UnsupportedEncodingException {
        StringBuilder url = new StringBuilder();
        url.append("https://nid.naver.com/oauth2.0/authorize?");
        url.append("client_id=").append(naverClientId);
        url.append("&redirect_uri=").append(URLEncoder.encode(naverRedirectUri, "UTF-8"));
        url.append("&response_type=code");
        return "redirect:" + url.toString();
    }

    private final JwtProvider jwtProvider;

    @Autowired
    public OAuthController(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    // Post 버전
    @Autowired
    private UsersService usersService;

    @PostMapping("/protected-resource")
    public ResponseEntity<UsersDto> postProtectedResource(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) throws InterruptedException, ExecutionException {
        // Authorization 헤더가 없는 경우
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // 토큰을 "Bearer " 부분을 제거하고 추출
        String token = authorizationHeader.substring(7);

        // JWT 검증
        String uid = jwtProvider.validate(token);
        if (uid == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // 사용자 정보를 조회
        UsersDto userDto = usersService.findByUid(uid);

        // 토큰이 유효한 경우 사용자 정보 제공
        return ResponseEntity.ok(userDto);
    }
    
}

