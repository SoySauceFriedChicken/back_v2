package com.example.Foodle.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Foodle.dto.request.user.NewUserDto;
import com.example.Foodle.dto.request.user.UpdateUserDto;
import com.example.Foodle.dto.request.user.UsersDto;
import com.example.Foodle.entity.UsersEntity;
import com.example.Foodle.service.UsersService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UsersController {

    private final UsersService usersService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllUsers() throws ExecutionException, InterruptedException {
        List<UsersDto> list = usersService.getAllUsers();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/profile")
    public UsersDto getuserprofile(@RequestParam String uid) throws ExecutionException, InterruptedException {
        return usersService.findByUid(uid);
    }

    @PostMapping("/create")
    public String createUser(@RequestBody @Valid NewUserDto newUserDto) {
        UsersEntity user = newUserDto.toEntity();
        try {
            usersService.saveUser(user);
            return "User created successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error creating user";
        }
        
    }

    @PostMapping("/update")
    public String updateUser(@RequestBody @Valid UsersDto updateUserDto) {
        try {
            usersService.updateUser(updateUserDto);
            return "User updated successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error updating user";
        }
        
    }

    // @GetMapping("/loginSuccess")
    // public String loginSuccess(@AuthenticationPrincipal OAuth2User oauth2User) {
    //     String email = oauth2User.getAttribute("email");
    //     String nickname = oauth2User.getAttribute("nickname");
    //     usersService.saveUserIfFirstLogin(email, nickname, nickname);
    //     return "redirect:/";
    // }

    // @GetMapping("/loginFailure")
    // public String loginFailure() {
    //     return "loginFailure";
    // }
}

