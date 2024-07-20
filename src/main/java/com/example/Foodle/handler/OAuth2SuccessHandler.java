package com.example.Foodle.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.Foodle.entity.customOAuth2User;
import com.example.Foodle.provider.JwtProvider;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler{

    private final JwtProvider jwtProvider;
    
    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request, 
        HttpServletResponse response,
		Authentication authentication
        ) throws IOException, ServletException {
		    customOAuth2User oauth2User = (customOAuth2User) authentication.getPrincipal();

            String userId = oauth2User.getName();
            String token = jwtProvider.create(userId);

            // 리디렉션 URL에 유저 ID를 포함하여 전송
            String redirectUrl = String.format("http://3.39.156.254:8080/auth/oauth-response/%s/3600?userId=%s", token, userId);
            response.sendRedirect(redirectUrl);
            // http://localhost:8080/auth/oauth-response/eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrYWthb18zNTkzMjI5NjA4IiwiaWF0IjoxNzIxMjIwMDM2LCJleHAiOjE3MjEyMjM2MzZ9.FOWtl8u8oXLxaDcZ2SAb22LRThjQ6JqhJ0OmI-THA00/3600
            // http://localhost:8080/auth/oauth-response/eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYXZlcl9fN1Y5eHBGZ0pzcXIxMCIsImlhdCI6MTcyMTIyMjI1MSwiZXhwIjoxNzIxMjI1ODUxfQ.Ita9eevVrisKpgcxOiCeXjPRn4G83eY1EhAfuaOsXi0/3600
	}
}



// package com.example.Foodle.handler;

// import org.springframework.security.core.Authentication;
// import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
// import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
// import org.springframework.stereotype.Component;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import java.io.IOException;
// import java.util.Map;

// import com.example.Foodle.dao.UsersDao;
// import com.example.Foodle.service.UsersService;
// import jakarta.servlet.ServletException;

// @Component
// public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

//     private final UsersService usersService;

//     public OAuth2SuccessHandler(UsersService usersService) {
//         this.usersService = usersService;
//     }

//     @Override
//     public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//         OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
//         Map<String, Object> attributes = token.getPrincipal().getAttributes();
        
//         // Print login information to console
//         System.out.println("로그인 성공: " + attributes);

//         String uid = (String) attributes.get("uid");
//         String nickname = (String) attributes.get("name");

//         UsersDao usersDao = new UsersDao();
//         // Save user data if first login
//         usersDao.saveUserIfFirstLogin(uid, nickname, nickname);

//         super.onAuthenticationSuccess(request, response, authentication); // Call superclass method
//     }
// }
