package konkuk.aiku.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NoSuchEntityException extends RuntimeException {
    private final ErrorCode errorCode;
}
