package konkuk.aiku.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.MDC;

@Getter
public class ErrorDTO {
    private int code;
    private String message;

    public ErrorDTO(int code, String message) {
        this.code = code;
        this.message = message + "[" + MDC.get("request_id") + "]";
    }
}
