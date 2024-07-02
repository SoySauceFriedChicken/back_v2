// package com.example.Foodle.service;

// import org.springframework.http.ResponseEntity;

// import com.example.Foodle.dto.request.auth.CheckCertificationRequestDto;
// import com.example.Foodle.dto.response.auth.CheckCertificationResponseDto;
// import com.example.Foodle.dto.request.auth.EmailCertificationRequestDto;
// import com.example.Foodle.dto.response.auth.EmailCertificationResponseDto;
// import com.example.Foodle.dto.request.auth.IdCheckRequestDto;
// import com.example.Foodle.dto.request.auth.SignUpRequestDto;
// import com.example.Foodle.dto.response.auth.IdCheckResponseDto;
// import com.example.Foodle.dto.response.auth.SignUpResponseDto;
// import com.example.Foodle.dto.request.auth.SignInRequestDto;
// import com.example.Foodle.dto.response.auth.SignInResponseDto;

// public interface AuthService {
//     ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto idCheckRequestDto);
//     ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto emailCertificationRequestDto);
//     ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto checkCertificationRequestDto);
//     ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto);
//     ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto);
// }
