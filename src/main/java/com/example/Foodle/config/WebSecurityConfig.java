// package com.example.Foodle.config;

// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.beans.factory.annotation.Configurable;
// import lombok.RequiredArgsConstructor;

// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
// import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
// import org.springframework.security.web.AuthenticationEntryPoint;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// // import org.springframework.web.cors.SessionCreationPolicy;
// import org.springframework.context.annotation.Bean;

// import com.example.Foodle.filter.JwtAuthenticationFilter;
// import com.example.Foodle.handler.OAuth2SuccessHandler;

// import java.io.IOException;

// @EnableWebSecurity
// @Configuration
// @Configurable
// @RequiredArgsConstructor

// public class WebSecurityConfig {
//     private final JwtAuthenticationFilter jwtAuthenticationFilter;
//     private final DefaultOAuth2UserService oAuth2UserService;
//     private final OAuth2SuccessHandler oAuth2SuccessHandler;

//     @SuppressWarnings("deprecation")
//     @Bean
//     protected SecurityFilterChain configure(HttpSecurity http, HttpSecurity httpSecurity) throws Exception{
//         httpSecurity
//         .cors(cors -> cors
//             .configurationSource(corsConfigurationSource())
//         )
//         .csrf((CsrfConfigurer::disable))
//         .httpBasic(HttpBasicConfigurer::disable)
//         .sessionManagement(sessionManagement -> sessionManagement
//             .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//         )
//         .authorizeRequests(request -> request
//             .requestMatchers("/", "/api/v1/auth/**", "/oauth2/**", "/oauth2/callback/**", "/api/v1/auth/oauth2/**", "/v1/**").permitAll()
//             .requestMatchers("/api/v1/user/**").hasRole("USER")
//             .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
//             .anyRequest().authenticated()
//         )
//         .oauth2Login(oauth2 -> oauth2
//             // .authorizationEndpoint(endpoint -> endpoint.baseUri("/api/v1/auth/oauth2"))
//             // .loginProcessingUrl("/api/v1/oauth2/*")
//             .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*"))
//             .userInfoEndpoint(endpoint -> endpoint.userService(oAuth2UserService))
//             .successHandler(oAuth2SuccessHandler)
//         )
//         .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(new FailedAuthenticationEntryPoint()))
//         .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


//         return httpSecurity.build();
//     }
//     @Bean 
//     protected CorsConfigurationSource corsConfigurationSource(){
//         CorsConfiguration configuration = new CorsConfiguration();
//         configuration.addAllowedOrigin("*");
//         configuration.addAllowedMethod("*");
//         configuration.addAllowedHeader("*");
 
//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         source.registerCorsConfiguration("/api/v1/**", configuration);
//         return source;
//     }
//     class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint{
//         @Override
//         public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//             response.setContentType("application/json");
//             response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//             // {"code": "NP", "message":"No Permission"}
//             response.getWriter().write("{\"code\": \"NP\", \"message\":\"No Permissions\"}");
//         }

//     }
// }
