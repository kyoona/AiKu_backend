package konkuk.aiku.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AlreadyInException extends RuntimeException{
    private final ErrorCode errorCode;
}
