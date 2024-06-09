package konkuk.aiku.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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

    @ExceptionHandler({AlreadyArrivalException.class})
    public ResponseEntity<ErrorDTO> handleAlreadyArrivalException(AlreadyArrivalException e){
        log.error("ERROR", e);
        return new ResponseEntity<>(new ErrorDTO(e.getErrorCode().getCode(), e.getErrorCode().getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MessagingException.class})
    public ResponseEntity<ErrorDTO> handleMessagingException(MessagingException e){
        log.error("ERROR", e);
        return new ResponseEntity<>(new ErrorDTO(e.getErrorCode().getCode(), e.getErrorCode().getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({VoiceApiException.class})
    public ResponseEntity<ErrorDTO> voiceException(VoiceApiException e){
        log.error("ERROR", e);
        return new ResponseEntity<>(new ErrorDTO(e.getErrorCode().getCode(), e.getErrorCode().getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 아래 내용 모두 추가
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorDTO> handleSignatureException(SignatureException e) {
        log.error("ERROR", e);
        return new ResponseEntity<>(new ErrorDTO(NOT_AVAILABLE_TOKEN.getCode(), NOT_AVAILABLE_TOKEN.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ErrorDTO> handleMalformedJwtException(MalformedJwtException e) {
        log.error("ERROR", e);
        return new ResponseEntity<>(new ErrorDTO(WRONG_TOKEN_TYPE.getCode(), WRONG_TOKEN_TYPE.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorDTO> handleExpiredJwtException(ExpiredJwtException e) {
        log.error("ERROR", e);
        return new ResponseEntity<>(new ErrorDTO(EXPIRED_TOKEN.getCode(), EXPIRED_TOKEN.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDTO> badCredentialsException(BadCredentialsException e) {
        log.error("ERROR", e);
        return new ResponseEntity<>(new ErrorDTO(BAD_CREDENTIALS.getCode(), BAD_CREDENTIALS.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorDTO> handleException(Exception e){
        log.error("ERROR", e);
        return new ResponseEntity<>(new ErrorDTO(INTERNAL_SERVER_ERROR.getCode(), INTERNAL_SERVER_ERROR.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}