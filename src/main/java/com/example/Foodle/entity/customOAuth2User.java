package com.example.Foodle.entity;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
public class customOAuth2User implements OAuth2User{

    private String uid;
    // private Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getName() {
        return this.uid;
    }
    
}
