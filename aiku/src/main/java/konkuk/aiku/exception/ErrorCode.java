package konkuk.aiku.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    //4XX Client error response
    //400 Bad Request

    //404 NOT FOUND
    NO_SUCH_ENTITY(404, "존재 하지 않는 데이터입니다."),

    //5XX Server error response
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류");

    private int code;
    private String message;
}
