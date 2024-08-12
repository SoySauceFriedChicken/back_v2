package com.example.Foodle.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Map<String, Object>> getUserProfile(@RequestParam String uid) {
        Map<String, Object> response = new HashMap<>();
        try {
            UsersDto user = usersService.findByUid(uid);
            if (user != null) {
                response.put("success", true);
                response.put("error", null);
                response.put("message", "User profile retrieved successfully");
                response.put("status", HttpStatus.OK.value());
                response.put("data", user);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("success", false);
                response.put("error", "User not found");
                response.put("message", "No user found with the provided UID");
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Error retrieving user profile");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("data", new HashMap<>());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody @Valid UsersDto newUserDto) {
        UsersEntity user = newUserDto.toEntity();
        Map<String, Object> response = new HashMap<>();
        try {
            String result = usersService.saveUser(user);
            if ("User saved successfully!".equals(result)) {
                response.put("success", true);
                response.put("error", null);
                response.put("message", result);
                response.put("status", HttpStatus.OK.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("success", false);
                response.put("error", result);
                response.put("message", "Error creating user");
                response.put("status", HttpStatus.BAD_REQUEST.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Error creating user");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("data", new HashMap<>());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> updateUser(@RequestBody @Valid UsersDto updateUserDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            String result = usersService.updateUser(updateUserDto);
            if ("User updated successfully!".equals(result)) {
                response.put("success", true);
                response.put("error", null);
                response.put("message", result);
                response.put("status", HttpStatus.OK.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else if ("no user found".equals(result)) {
                response.put("success", false);
                response.put("error", result);
                response.put("message", "User not found");
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                response.put("success", false);
                response.put("error", result);
                response.put("message", "Error updating user");
                response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Error updating user");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("data", new HashMap<>());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteUser(@RequestParam String uid) {
        Map<String, Object> response = new HashMap<>();
        try {
            String result = usersService.deleteUser(uid);
            if ("User deleted successfully!".equals(result)) {
                response.put("success", true);
                response.put("error", null);
                response.put("message", result);
                response.put("status", HttpStatus.OK.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else if ("no user found".equals(result)) {
                response.put("success", false);
                response.put("error", result);
                response.put("message", "User not found");
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                response.put("success", false);
                response.put("error", result);
                response.put("message", "Error deleting user");
                response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.put("data", new HashMap<>());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("message", "Error deleting user");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("data", new HashMap<>());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
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

