package konkuk.aiku.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class S3Exception extends RuntimeException{
    private final ErrorCode errorCode;
}
