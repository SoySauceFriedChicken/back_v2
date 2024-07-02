// package com.example.Foodle.service.implement;

// import com.example.Foodle.dto.response.ResponseDto;
// import com.example.Foodle.dto.response.auth.IdCheckResponseDto;
// import com.example.Foodle.dto.response.auth.SignInResponseDto;
// import com.example.Foodle.entity.CertificationEntity;
// import com.example.Foodle.entity.UserEntity;
// import com.example.Foodle.provider.EmailProvider;
// import com.example.Foodle.provider.JwtProvider;
// import com.example.Foodle.repository.CertificationRepository;
// import com.example.Foodle.repository.UserRepository;
// import com.example.Foodle.service.AuthService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;

// import com.example.Foodle.common.CertificationNumber;
// import com.example.Foodle.dto.request.auth.CheckCertificationRequestDto;
// import com.example.Foodle.dto.response.auth.CheckCertificationResponseDto;
// import com.example.Foodle.dto.request.auth.EmailCertificationRequestDto;
// import com.example.Foodle.dto.response.auth.EmailCertificationResponseDto;
// import com.example.Foodle.dto.request.auth.IdCheckRequestDto;
// import com.example.Foodle.dto.request.auth.SignInRequestDto;
// import com.example.Foodle.dto.request.auth.SignUpRequestDto;
// import com.example.Foodle.dto.response.auth.SignUpResponseDto;

// @Service
// @RequiredArgsConstructor
// public class AuthServiceImplement implements AuthService{

//     private final CertificationRepository certificationRepository;
//     private final UserRepository userRepository;
//     private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//     private final JwtProvider jwtProvider;
//     private final EmailProvider emailProvider;

//     @Override
//     public ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto) {
//         try{
//             String id = dto.getId();
//             boolean isExistId =  userRepository.existsById(id);
//             if(isExistId){
//                 return IdCheckResponseDto.duplicateId();
//             }

//         }catch(Exception e){
//             e.printStackTrace();
//             return ResponseDto.databaseError();
//         }
//         return IdCheckResponseDto.idCheckSuccess();
//     }

//     @Override
//     public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto) {
//         try{
//             String userId = dto.getId();
//             String email = dto.getEmail();
            
//             boolean isExistId =  userRepository.existsById(userId);
//             if(isExistId){
//                 return EmailCertificationResponseDto.duplicateId();
//             }

//             String certificationNumber = CertificationNumber.getCertificationNumber();

//             boolean isSuccessed = emailProvider.sendCertificationMail(email, certificationNumber);
//             if(!isSuccessed){
//                 return EmailCertificationResponseDto.mailSendFail();
//             }

//             CertificationEntity certificationEntity = new CertificationEntity(userId, certificationNumber, certificationNumber);
//             certificationRepository.save(certificationEntity);

//         }catch(Exception e){
//             e.printStackTrace();
//             return ResponseDto.databaseError();
//         }

//         return EmailCertificationResponseDto.success();
//     }

//     @Override
//     public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto) {
//         try{
//             String userId = dto.getId();
//             String email = dto.getEmail();
//             String certificationNumber = dto.getCertificationNumber();

//             CertificationEntity certificationEntity = certificationRepository.findByUid(userId);
//             if(certificationEntity == null){
//                 return CheckCertificationResponseDto.certificationFail();
//             }

//             boolean isMatched = certificationEntity.getEmail().equals(email) && certificationEntity.getCertificationNumber().equals(certificationNumber);
//             if(!isMatched){
//                 return CheckCertificationResponseDto.certificationFail();
//             }

//         }catch(Exception e){
//             e.printStackTrace();
//             return ResponseDto.databaseError();
//         }
//         return CheckCertificationResponseDto.success();
//     }

//     @Override
//     public ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto) {
//         try{
//             String userId = dto.getId();

//             boolean isExistId =  userRepository.existsById(userId);
//             if(isExistId){
//                 return SignUpResponseDto.duplicateId();
//             }
//             String email = dto.getEmail();
//             String certificationNumber = dto.getCertificationNumber();

//             CertificationEntity certificationEntity = certificationRepository.findByUid(userId);
//             boolean isMatched = certificationEntity.getEmail().equals(email) && certificationEntity.getCertificationNumber().equals(certificationNumber);

//             if(!isMatched){
//                 return SignUpResponseDto.certificationFail();
//             }

//             String password = dto.getPassword();
//             String encodedPassword = passwordEncoder.encode(password);
//             dto.setPassword(encodedPassword);

//             UserEntity userEntity = new UserEntity(dto);
//             userRepository.save(userEntity);

//             certificationRepository.deleteByUid(certificationEntity);

//         }catch(Exception e){
//             e.printStackTrace();
//             return ResponseDto.databaseError();
//         }
//         return SignUpResponseDto.success("token");
//     }

//     @Override
//     public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto) {
//         String token = null;
//         try{
//             String userId = dto.getUid();

//             UserEntity userEntity = userRepository.findByuid(userId);
//             if(userEntity == null){
//                 return SignInResponseDto.signInFail();
//             }

//             String password = dto.getPassword();
//             String encodedPassword = userEntity.getPassword();

//             boolean isMatched = passwordEncoder.matches(password, encodedPassword);
//             if(!isMatched){
//                 return SignInResponseDto.signInFail();
//             }

//             token = jwtProvider.create(userId);

//         }catch(Exception e){
//             e.printStackTrace();
//             return ResponseDto.databaseError();
//         }
//         return SignInResponseDto.success(token);
//     }

    
// }
