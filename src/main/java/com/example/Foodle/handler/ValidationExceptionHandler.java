// package com.example.Foodle.handler;

// import com.example.Foodle.dto.response.ResponseDto;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.MethodArgumentNotValidException;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.RestControllerAdvice;
// import org.springframework.http.converter.HttpMessageNotReadableException;

// @RestControllerAdvice
// public class ValidationExceptionHandler {
//     @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
//     public ResponseEntity<ResponseDto> validationExceptionHandler(Exception exception){
//         return ResponseDto.validationFail();
//     }
// }
