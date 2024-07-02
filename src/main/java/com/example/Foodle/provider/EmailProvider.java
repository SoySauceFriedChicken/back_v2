// package com.example.Foodle.provider;

// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.mail.javamail.MimeMessageHelper;
// import org.springframework.stereotype.Component;

// import jakarta.mail.internet.MimeMessage;
// import lombok.RequiredArgsConstructor;

// @Component
// @RequiredArgsConstructor
// public class EmailProvider {
//     private final JavaMailSender javaMailSender;

//     private final String SUBJECT = "[Foodle] 회원가입 인증 메일입니다.";

//     public boolean sendCertificationMail(String email, String certificationNumber) {
//         // 이메일 전송 로직
        
//         try{
//             MimeMessage message = javaMailSender.createMimeMessage();
//             MimeMessageHelper helper = new MimeMessageHelper(message, true);

//             String certificationMessage = getCertificationNumber(certificationNumber);
//             helper.setSubject(SUBJECT);
//             helper.setTo(email);
//             helper.setText(certificationMessage);
//             javaMailSender.send(message);
//         }
//         catch(Exception e){
//             e.printStackTrace();
//             return false;
//         }



//         return true;
//     }
//     private String getCertificationNumber(String certificationNumber){
//         // 인증번호 생성 로직
//         String certificationMessage = "";
//         certificationMessage += "<h1>인증메일</h1>";
//         certificationMessage += "<p>인증번호를 입력해주세요.</p>" + certificationNumber + "<br>";
//         return certificationMessage;
//     }
// }
