package konkuk.aiku.controller.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class SuccessResponseDto {
    private final Long id;
    private final boolean success = true;
    private String message;

    public SuccessResponseDto(Long id, SuccessMessage successMessage) {
        this.id = id;
        this.message = successMessage.message;
    }

    public static ResponseEntity<SuccessResponseDto> getResponseEntity(Long id, SuccessMessage message, HttpStatus status){
        return new ResponseEntity<>(new SuccessResponseDto(id, message), status);
    }

    public enum SuccessMessage {
        LOGOUT_SUCCESS("로그아웃 성공하였습니다."),
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
