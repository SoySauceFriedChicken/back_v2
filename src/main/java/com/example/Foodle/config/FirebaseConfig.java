package com.example.Foodle.config;
import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

@Configuration
public class FirebaseConfig {

    public static final String API_URL = "https://fcm.googleapis.com/v1/projects/foodle-29946/messages:send";

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        var resource = new ClassPathResource("serviceAccountKey.json");
        var serviceAccount = resource.getInputStream();
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        FirebaseApp app = FirebaseApp.initializeApp(options);
        System.out.println("FirebaseApp initialized: " + app);
        return app;
    }

    // @Bean
    // public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
    //     return FirebaseMessaging.getInstance(firebaseApp);
    // }
}