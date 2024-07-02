// package com.example.Foodle.dto.response.auth;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;

// import com.example.Foodle.dto.response.ResponseDto;

// import lombok.Getter;

// @Getter
// public class EmailCertificationResponseDto extends ResponseDto{
//     private EmailCertificationResponseDto(){
//         super();
//     }
//     public static ResponseEntity<EmailCertificationResponseDto> success(){
//         EmailCertificationResponseDto responseBody = new EmailCertificationResponseDto();
//         return ResponseEntity.status(HttpStatus.OK).body(responseBody);
//     }

//     public static ResponseEntity<ResponseDto> duplicateId(){
//         ResponseDto responseBody = new ResponseDto("DUPLICATE_ID", "중복된 아이디입니다.");
//         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
//     }
//     public static ResponseEntity<ResponseDto> mailSendFail(){
//         ResponseDto responseBody = new ResponseDto("MAIL_FAIL", "메일 전송에 실패하였습니다.");
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
//     }
// }
