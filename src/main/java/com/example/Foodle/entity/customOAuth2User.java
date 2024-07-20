package com.example.Foodle.entity;

import java.util.Collection;
import java.util.Map;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class customOAuth2User implements OAuth2User {

    private String uid;
    private Map<String, Object> attributes;
    private Collection<? extends GrantedAuthority> authorities;

    public customOAuth2User(String uid, Map<String, Object> attributes) {
        this.uid = uid;
        this.attributes = attributes;
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public customOAuth2User(String uid) {
        this.uid = uid;
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return this.uid;
    }
}
