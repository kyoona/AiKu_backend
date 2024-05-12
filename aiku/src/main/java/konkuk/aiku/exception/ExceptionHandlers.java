package konkuk.aiku.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static konkuk.aiku.exception.ErrorCode.*;

@RestControllerAdvice
public class ExceptionHandlers  {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorDTO> handleExampleException(Exception ex){
        ex.printStackTrace();
        return new ResponseEntity<>(new ErrorDTO(INTERNAL_SERVER_ERROR.getCode(), INTERNAL_SERVER_ERROR.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}