package konkuk.aiku.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class NoSuchEntityException extends RuntimeException {
    private final ErrorCode errorCode;
}
