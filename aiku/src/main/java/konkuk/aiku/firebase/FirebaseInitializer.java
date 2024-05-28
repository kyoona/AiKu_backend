package konkuk.aiku.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;

@Component
@Slf4j
public class FirebaseInitializer {
    @PostConstruct
    public void initialize(){
        try {
            FileInputStream serviceAccount = new FileInputStream("src/main/java/konkuk/aiku/firebase/aiku-18cc3-firebase-adminsdk-akm67-c379d3ded3.json");

            /*우분투 환경에서 절대 경로*/
//            FileInputStream serviceAccount = new FileInputStream("/home/ubuntu/aiku_backend/AiKu_backend/aiku/src/main/java/konkuk/aiku/firebase/aiku-18cc3-firebase-adminsdk-akm67-c379d3ded3.json");
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp app = FirebaseApp.initializeApp(options);
            log.info("FirebaseInitializer app={}", app.getName());
        } catch (IOException e) {
            throw new RuntimeException("FirebaseInitializer 오류", e);
        }
    }
}
