package com.example.Foodle.service;

import org.springframework.stereotype.Service;
import com.example.Foodle.config.FirebaseConfig;
import com.example.Foodle.dto.request.push.FCMMessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.json.Json;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.http.HttpHeaders;
import org.springframework.core.io.ClassPathResource;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import java.io.IOException;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import okhttp3.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {
    // private final FirebaseMessaging firebaseMessaging;
    // private List<String> targetUserTokens; // 추가: FCM 토큰 리스트
    private final ObjectMapper objectMapper;

    public static String getAccessToken() throws IOException {
            GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource("serviceAccountKey.json").getInputStream())
                .createScoped("https://www.googleapis.com/auth/cloud-platform");
            googleCredentials.refreshIfExpired();
            return googleCredentials.getAccessToken().getTokenValue();
    }

    /*
     * MakeMessage: 알림 파라미터들을 FCM이 요구하는 body 형태로 가공한다
     * @param targetToken: firebase token
     * @param title: 알림 제목
     * @param body: 알림 내용
     * @return
     */
     public String makeMessage(
        String targetToken, String title, String body, String name, String description
        ) throws JsonProcessingException {
            FCMMessageDto fcmMessageDto = FCMMessageDto.builder()
                .validateOnly(false)
                .message(FCMMessageDto.Message.builder()
                    .token(targetToken)
                    .notification(FCMMessageDto.Notification.builder()
                        .title(title)
                        .body(body)
                        .build())
                    .data(FCMMessageDto.Data.builder()
                        .name(name)
                        .description(description)
                        .build())
                    .build())
                .build();

            return objectMapper.writeValueAsString(fcmMessageDto);
        }

    /*
     * 알림 푸쉬를 보내는 역할을 하는 메서드
     * @param targetToken: 푸쉬 알림을 받을 클라이언트 앱의 식별 토큰
     */
    public void sendPushMessage(String targetToken, String title, String body, String id, String isEnd) throws IOException, JsonProcessingException {
        String message = makeMessage(targetToken, title, body, id, isEnd);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        
        Request request = new Request.Builder()
            .url(FirebaseConfig.API_URL)
            .post(requestBody)
            .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
            .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
            .build();

        Response response = client.newCall(request).execute();
        log.info("FCM response: {}", response.body().string());
        return;
    }
    



    // public void sendPushMessage(String userToken, PushRequestDto pushRequestDto) throws FirebaseMessagingException {
    //     // Send push message
    //     firebaseMessaging.send(makeMessage(userInfo.getUserPushToken(), pushRequestDto.getTitle(), pushRequestDto.getContent()));
    // }


    // public static Message makeMessage(String token, String title, String content) {
    //     Notification notification = Notification.builder().setTitle(title).setBody(content).build();
    //     return Message
    //         .builder()
    //         .setNotification(notification)
    //         .setToken(token)
    //         .build();
    // }

    // public void sendPushMessageToAll(PushRequestDto pushRequestDto) throws FirebaseMessagingException {
    //     // Send push message to all users
    //     firebaseMessaging.sendAll(makeMessages(pushRequestDto.getTitle(), pushRequestDto.getContent(), targetUserTokens));
    // }


    // public static MulticastMessage makeMessages(String title, String content) {
    //     Notification notification = Notification.builder().setTitle(title).setBody(content).build();
    //     return MulticastMessage
    //         .builder()
    //         .setNotification(notification)
    //         .addAllTokens(targetUserTokens)
    //         .build();
    // }

    // // targetUserTokens 초기화 메서드 (예: DB에서 읽어오는 로직 필요)
    // public void setTargetUserTokens(List<String> tokens) {
    //     this.targetUserTokens = tokens;
    // }
}
