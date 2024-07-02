// package com.example.Foodle.dto.response.auth;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;

// import com.example.Foodle.dto.response.ResponseDto;

// import lombok.Getter;

// @Getter
// public class CheckCertificationResponseDto extends ResponseDto {
//     private CheckCertificationResponseDto() {
//         super();
//     }
//     public static ResponseEntity<CheckCertificationResponseDto> success(){
//         CheckCertificationResponseDto responseBody = new CheckCertificationResponseDto();
//         return ResponseEntity.status(HttpStatus.OK).body(responseBody);
//     }
//     public static ResponseEntity<ResponseDto> certificationFail(){
//         ResponseDto responseBody = new ResponseDto("CERTIFICATION_FAIL", "인증에 실패하였습니다.");
//         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
//     }
// }
