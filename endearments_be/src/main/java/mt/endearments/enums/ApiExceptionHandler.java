package mt.endearments.enums;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handleApiException(ApiException ex) {
        ErrorCode error = ex.getErrorCode();
        return ResponseEntity
                .status(error.getStatus())
                .body(Map.of(
                        "code", error.getCode(),
                        "message", error.getMessage(),
                        "status", error.getStatus().value()
                ));
    }
}
