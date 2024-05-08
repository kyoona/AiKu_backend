package konkuk.aiku.controller.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SuccessResponseDto {
    private final boolean success = true;
    private SuccessMessage message;

    public SuccessResponseDto(SuccessMessage message) {
        this.message = message;
    }

    public static ResponseEntity<SuccessResponseDto> getResponseEntity(SuccessMessage message, HttpStatus status){
        return new ResponseEntity<>(new SuccessResponseDto(message), status);
    }

    public enum SuccessMessage {
        ADD_SUCCESS("등록 성공하였습니다."),
        MODIFY_SUCCESS("수정 성공하였습니다."),
        DELETE_SUCCESS("삭제 성공하였습니다."),
        EXIT_SUCCESS("퇴장 성공하였습니다."),
        ENTER_SUCCESS("입장 성공하였습니다.");

        private String message;

        SuccessMessage(String message) {
            this.message = message;
        }
    }
}
