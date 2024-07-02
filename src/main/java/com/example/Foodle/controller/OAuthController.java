// package com.example.Foodle.controller;

// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.RequestMapping;

// import com.google.api.client.util.Value;

// import lombok.NoArgsConstructor;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;




// @Controller
// @NoArgsConstructor
// @RequestMapping("/member")
// public class OAuthController {
//     @Value("${kako.client.id}")
//     String kakaoClientId;
//     @Value("${kakao.redirect.uri}")
//     String kakaoRedirectUri;
//     @Value("${kakao.client.secret}")
//     String kakaoClientSecret;

//     /*
//      * 카카오 로그인 요청
//      * @return
//      */
//     @GetMapping("/kakao")
//     public String kakaoConnet() {
//         StringBuffer url = new StringBuffer();
//         url.append("https://kauth.kakao.com/oauth/authorize?");
//         url.append("client_id=").append(kakaoClientId);
//         url.append("&redirect_uri=").append(kakaoRedirectUri);
//         url.append("&response_type=code");
//         return "redirect:" + url.toString();
//     }
    
// }
