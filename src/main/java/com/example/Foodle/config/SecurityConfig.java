/* 

package com.example.Foodle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.web.SecurityFilterChain;

import com.example.Foodle.handler.OAuth2SuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfiguration{

    private final OAuth2SuccessHandler successHandler;

    public SecurityConfig(OAuth2SuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @SuppressWarnings("deprecation")
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(requests -> requests
                        .anyRequest().authenticated())
                .oauth2Login(oauth2Login -> oauth2Login
                        .successHandler(successHandler));
    }
}

*/