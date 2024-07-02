// package com.example.Foodle.dto.response.auth;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.HttpStatusCode;
// import org.springframework.http.ResponseEntity;

// import com.example.Foodle.common.ResponseCode;
// import com.example.Foodle.dto.response.ResponseDto;

// import lombok.Getter;

// @Getter
// public class SignInResponseDto extends ResponseDto{

//     private String token;
//     private int expirationTime;


//     private SignInResponseDto(String token){
//         super();
//         this.token = token;
//         this.expirationTime = 3600;
//     } 

//     public static ResponseEntity<SignInResponseDto> success(String token){
//         SignInResponseDto responseBody = new SignInResponseDto(token);
//         return ResponseEntity.status(HttpStatus.OK).body(responseBody);

//     }
//     public static ResponseEntity<ResponseDto> signInFail(){
//         ResponseDto responseBody = new ResponseDto(ResponseCode.SIGN_IN_FAIL, ResponseCode.SIGN_IN_FAIL);
//         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
//     }
    
// }
