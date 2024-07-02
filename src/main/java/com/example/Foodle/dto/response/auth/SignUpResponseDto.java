// package com.example.Foodle.dto.response.auth;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;

// import com.example.Foodle.common.ResponseCode;
// import com.example.Foodle.common.ResponseMessage;
// import com.example.Foodle.dto.response.ResponseDto;

// import lombok.Getter;

// @Getter
// public class SignUpResponseDto extends ResponseDto{

//     private String token;
//     private int expirationTime;

//     private SignUpResponseDto(String token){
//         super();
//         this.token = token;
//         this.expirationTime = 3600;
//     }

//     public static ResponseEntity<SignUpResponseDto> success(String token){
//         SignUpResponseDto responseBody = new SignUpResponseDto(token);
//         return ResponseEntity.status(HttpStatus.OK).body(responseBody);

//     }
//     public static ResponseEntity<ResponseDto> duplicateId(){
//         ResponseDto responseBody = new ResponseDto(ResponseCode.DUPLICATE_ID, ResponseMessage.DUPLICATE_ID);
//         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
//     }
//     public static ResponseEntity<ResponseDto> certificationFail(){
//         ResponseDto responseBody = new ResponseDto(ResponseCode.CERTIFICATION_FAIL, ResponseMessage.CERTIFICATION_FAIL);
//         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
//     }
// }
