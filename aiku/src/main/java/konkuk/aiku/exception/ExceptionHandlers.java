package konkuk.aiku.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static konkuk.aiku.exception.ErrorCode.*;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlers  {

    @ExceptionHandler({AlreadyInException.class})
    public ResponseEntity<ErrorDTO> handleAlreadyInException(AlreadyInException e){
        log.error("ERROR", e);
        return new ResponseEntity<>(new ErrorDTO(e.getErrorCode().getCode(), e.getErrorCode().getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NoSuchEntityException.class})
    public ResponseEntity<ErrorDTO> handleNoSuchEntityException(NoSuchEntityException e){
        log.error("ERROR", e);
        return new ResponseEntity<>(new ErrorDTO(e.getErrorCode().getCode(), e.getErrorCode().getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NoAthorityToAccessException.class})
    public ResponseEntity<ErrorDTO> handleNoAthorityToAccessException(NoAthorityToAccessException e){
        log.error("ERROR", e);
        return new ResponseEntity<>(new ErrorDTO(e.getErrorCode().getCode(), e.getErrorCode().getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MessagingException.class})
    public ResponseEntity<ErrorDTO> handleMessagingException(MessagingException e){
        log.error("ERROR", e);
        return new ResponseEntity<>(new ErrorDTO(e.getErrorCode().getCode(), e.getErrorCode().getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorDTO> handleException(Exception e){
        log.error("ERROR", e);
        return new ResponseEntity<>(new ErrorDTO(INTERNAL_SERVER_ERROR.getCode(), INTERNAL_SERVER_ERROR.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}