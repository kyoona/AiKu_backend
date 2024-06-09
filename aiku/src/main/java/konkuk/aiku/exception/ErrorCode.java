package konkuk.aiku.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    //4XX Client error response
    //400 Bad Request
    ALREADY_IN_GROUP(4001, "이미 챰여중인 그룹입니다."),
    ALREADY_IN_SCHEDULE(4002, "이미 챰여중인 그룹입니다"),
    DUPLICATE_FCM_TOKEN(4003, "등록된 토큰이 존재합니다."),
    NO_VALIDATE_FCM_TOKEN(4004, "유효하지 않은 토큰입니다."),
    EXPIRATION_TOKEN(4005, "만료된 토큰입니다."),
    NO_GENERATED_TOKEN(4006, "등록되어 있는 토큰이 존재하지 않습니다."),
    AlreadyArrivalException(4015, "이미 도착한 사용자입니다."),

    // VoiceApi에서 발생하는 오류
    PAST_DATE(4007, "과거의 날짜입니다."),
    NOT_AVAILABLE_DATE(4008, "존재하지 않는 날짜입니다."),

    NOT_AVAILABLE_TOKEN(4011, "토큰이 유효하지 않습니다."),
    WRONG_TOKEN_TYPE(4012, "올바르지 않은 토큰입니다."),
    EXPIRED_TOKEN(4013, "토큰이 만료되었습니다. 다시 로그인하세요."),
    BAD_CREDENTIALS(4014, "회원정보가 없습니다."),

    //403 Forbidden
    NO_ATHORITY_TO_ACCESS(4031, "데이터에 접근 권한이 없습니다."),
    SCHEDULE_TO_WAIT(4032, "아직 열리지 않은 스케줄입니다."),
    SCHEDULE_TO_TERM(4033, "종료된 스케줄입니다."),

    //404 NOT FOUND
    NO_SUCH_ENTITY(4041, "존재하지 않는 데이터입니다."),
    NO_SUCH_USER(4042, "존재하지 않는 유저입니다."),
    NO_SUCH_SCHEDULE(4043, "존재하지 않는 스케줄입니다."),
    NO_SUCH_GROUP(4044, "존재하지 않는 그룹입니다."),
    NO_SUCH_ORDER(4045, "존재하지 않는 주문입니다."),
    NO_SUCH_BETTING(4046, "존재하지 않는 베팅입니다."),
    NO_SUCH_TITLE(4047, "존재하지 않는 칭호입니다."),
    NO_SUCH_ITEM(4048, "존재하지 않는 칭호입니다."),

    //5XX Server error response
    INTERNAL_SERVER_ERROR(5001, "서버 내부 오류"),
    FAIL_TO_CONVERT_MESSAGE(5002, "Message를 Firebase Data로 변환 실패했습니다."),
    FAIL_TO_SEND_MESSAGE(5003, "Firebase 메세지 전송 실패");

    private int code;
    private String message;
}
