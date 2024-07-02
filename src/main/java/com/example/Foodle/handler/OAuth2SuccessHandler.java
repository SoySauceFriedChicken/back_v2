// package com.example.Foodle.handler;

// import org.springframework.security.core.Authentication;
// import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
// import org.springframework.stereotype.Component;

// import com.example.Foodle.entity.customOAuth2User;
// import com.example.Foodle.provider.JwtProvider;

// import java.io.IOException;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import lombok.RequiredArgsConstructor;

// @Component
// @RequiredArgsConstructor
// public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler{

//     private final JwtProvider jwtProvider;
    
//     @Override
//     public void onAuthenticationSuccess(
//         HttpServletRequest request, 
//         HttpServletResponse response,
// 		Authentication authentication
//         ) throws IOException, ServletException {
// 		    customOAuth2User oauth2User = (customOAuth2User) authentication.getPrincipal();

//             String userId = oauth2User.getName();
//             String token = jwtProvider.create(userId);

//             response.sendRedirect("http://localhost:3000/auth/oauth-response/" + token + "/3600");
            

// 	}
// }




/* 
package com.example.Foodle.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import com.example.Foodle.service.UsersService;
import jakarta.servlet.ServletException;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UsersService usersService;

    public OAuth2SuccessHandler(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        Map<String, Object> attributes = token.getPrincipal().getAttributes();
        
        // Print login information to console
        System.out.println("로그인 성공: " + attributes);

        String uid = (String) attributes.get("uid");
        String nickname = (String) attributes.get("name");

        // Save user data if first login
        usersService.saveUserIfFirstLogin(uid, nickname, nickname);

        super.onAuthenticationSuccess(request, response, authentication); // Call superclass method
    }
}
*/