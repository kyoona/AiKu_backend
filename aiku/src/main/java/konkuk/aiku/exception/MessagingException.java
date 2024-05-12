package konkuk.aiku.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MessagingException extends RuntimeException{
    private final ErrorCode errorCode;
}
