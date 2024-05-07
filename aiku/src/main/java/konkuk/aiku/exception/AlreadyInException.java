package konkuk.aiku.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AlreadyInException extends RuntimeException{
    private final ErrorCode errorCode;
}
