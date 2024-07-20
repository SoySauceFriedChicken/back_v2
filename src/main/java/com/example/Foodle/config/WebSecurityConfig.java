package com.example.Foodle.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.Foodle.filter.JwtAuthenticationFilter;
import com.example.Foodle.handler.OAuth2SuccessHandler;
// import com.example.Foodle.handler.OAuth2SuccessHandler;
import com.example.Foodle.service.implement.OAuth2UserServiceImplement;

import java.io.IOException;

@Slf4j
@EnableWebSecurity
@Configurable
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final DefaultOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @SuppressWarnings({ "deprecation" })
    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // http
        //     .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        //     .csrf(CsrfConfigurer::disable)
        //     .httpBasic(HttpBasicConfigurer::disable)
        //     .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        //     .authorizeRequests(request -> request
        //         .requestMatchers("/", "/api/v1/auth/**", "/oauth2/**", "/oauth2/callback/**", "/api/v1/auth/oauth2/**", "/v1/**").permitAll()
        //         .requestMatchers("/api/v1/user/**").hasRole("USER")
        //         .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
        //         .anyRequest().authenticated()
        //     )
        //     // 소셜 로그인 설정
        //     .oauth2Login(oauth2 -> oauth2
        //         .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*"))
        //         .userInfoEndpoint(endpoint -> endpoint.userService(oAuth2SuccessHandler))
        //         // .successHandler(oAuth2SuccessHandler)
        //     )
        //     .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(new FailedAuthenticationEntryPoint()))
        //     .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // return http.build();
        log.info("Configuring HttpSecurity");

        http
            .cors(cors -> {
                log.info("Configuring CORS");
                cors.configurationSource(corsConfigurationSource());
            })
            .csrf(csrf -> {
                log.info("Disabling CSRF");
                csrf.disable();
            })
            .httpBasic(httpBasic -> {
                log.info("Disabling HTTP Basic");
                httpBasic.disable();
            })
            .sessionManagement(sessionManagement -> {
                log.info("Configuring Session Management");
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
            .authorizeRequests(requests -> {
                log.info("Configuring Authorization Requests");
                requests
                    .requestMatchers("/", "/api/**", "/api/v1/auth/**", "/oauth2/**", "/oauth2/callback/**", "/api/v1/auth/oauth2/**", "/v1/**", "/error").permitAll()
                    .requestMatchers("/auth/oauth-response/**").permitAll()
                    // .requestMatchers("/api/v1/user/**").hasRole("USER")
                    // .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated();
            })
            .oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(endpoint -> endpoint.baseUri("/api/v1/auth/oauth2"))
                .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*"))
                .userInfoEndpoint(endpoint -> endpoint.userService(oAuth2UserService))
                .successHandler(oAuth2SuccessHandler)
            )
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(new FailedAuthenticationEntryPoint())
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/v1/**", configuration);
        return source;
    }

    class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
            log.info("FailedAuthenticationEntryPoint.commence called. Request URL: {}", request.getRequestURL());
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"code\": \"NP\", \"message\":\"No Permissions\"}");
        }

    }
}
