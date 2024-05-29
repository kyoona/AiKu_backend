package konkuk.aiku.firebase;

import com.google.firebase.messaging.*;
import konkuk.aiku.exception.ErrorCode;
import konkuk.aiku.exception.MessagingException;
import konkuk.aiku.firebase.dto.RealTimeLocationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j
public class MessageSender {

    public void sendMessageToUsers(Map<String, String> messageDataMap, List<String> receiverTokens) {
        MulticastMessage message = MulticastMessage.builder()
                .putAllData(messageDataMap)
                .addAllTokens(receiverTokens)
                .build();

        FirebaseMessaging.getInstance().sendEachForMulticastAsync(message);
    }

    public void sendMessageToUser(Map<String, String> messageDataMap, String receiverToken) {
        Message message = Message.builder()
                .putAllData(messageDataMap)
                .setToken(receiverToken)
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            throw new MessagingException(ErrorCode.FAIL_TO_SEND_MESSAGE);
        }
    }

    public void testSend() {
        String token = "dUwihOGnQ4WMj-f8FL7Uid:APA91bGaRQU8HRfocNHR7V-jzVU5BPARhYdh0hcY1jAbkDTVJhO1qdyFcXYKtDhg34WJnnqqSgfIDHzGWiSnfIvPr_bN0J4khES2IZjpfTxL0ZNUQ1mU__9FthMM4ZZ-BoydPpssVSYN";

        Message message = Message.builder()
                .putData("title", "test")
                .setToken(token)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Message sent successfully: " + response);
        } catch (FirebaseMessagingException e) {
            System.err.println("Failed to send message: " + e.getMessage());
            throw new MessagingException(ErrorCode.FAIL_TO_SEND_MESSAGE);
        }
    }
}
