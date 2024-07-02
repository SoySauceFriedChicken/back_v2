// package com.example.Foodle.entity;

// import org.springframework.boot.autoconfigure.security.SecurityProperties.User;

// import com.example.Foodle.dto.request.auth.SignUpRequestDto;

// import jakarta.persistence.Entity;
// import jakarta.persistence.Id;
// import jakarta.persistence.Table;
// import lombok.AllArgsConstructor;
// import lombok.Getter;
// import lombok.NoArgsConstructor;

// @Getter
// @NoArgsConstructor
// @AllArgsConstructor
// @Entity(name="user")
// @Table(name="User")
// public class UserEntity {
//     @Id
//     private String uid;
//     private String name;
//     private String icon;
//     private String fid;
//     private String email;
//     private String password;
//     private String role;
//     private String type;

//     public String getRole() {
//         return "";
//     }
//     public UserEntity (SignUpRequestDto dto) {
//         this.uid = dto.getId();
//         this.password = dto.getPassword();
//         this.email = dto.getEmail();
//         this.role = "ROLE_USER";
//         this.type = "app";

//     }
//     public UserEntity (String uid, String email, String type) {
//         this.uid = uid;
//         this.email = email;
//         this.icon = "default";
//         this.fid = "default";
//         this.password = "pass";
//         this.role = "ROLE_USER";
//         this.type = type;
//     }
//     public String getFriendIds() {
//         // TODO Auto-generated method stub
//         throw new UnsupportedOperationException("Unimplemented method 'getFriendIds'");
//     }
// }
