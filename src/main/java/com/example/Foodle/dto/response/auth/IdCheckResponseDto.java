// package com.example.Foodle.dto.response.auth;

// import com.example.Foodle.common.ResponseCode;
// import com.example.Foodle.common.ResponseMessage;
// import com.example.Foodle.dto.response.ResponseDto;
// import lombok.Getter;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;

// @Getter
// public class IdCheckResponseDto extends ResponseDto{
//     private IdCheckResponseDto(){
//         super();
//     }

//     public static ResponseEntity<ResponseDto> idCheckSuccess(){
//         IdCheckResponseDto responseBody = new IdCheckResponseDto();
//         return ResponseEntity.status(HttpStatus.OK).body(responseBody);
//     }

//     public static ResponseEntity<ResponseDto> duplicateId(){
//         ResponseDto responseBody = new ResponseDto(ResponseCode.DUPLICATE_ID, ResponseMessage.DUPLICATE_ID);
//         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
//     }
// }
