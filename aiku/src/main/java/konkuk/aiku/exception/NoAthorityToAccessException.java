package konkuk.aiku.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NoAthorityToAccessException extends RuntimeException{
    private final ErrorCode errorCode;
}
