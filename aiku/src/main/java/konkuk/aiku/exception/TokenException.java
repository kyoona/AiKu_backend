package konkuk.aiku.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TokenException extends RuntimeException{
    private final ErrorCode errorCode;
}
