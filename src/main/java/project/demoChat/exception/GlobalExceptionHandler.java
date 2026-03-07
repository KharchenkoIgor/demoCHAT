package project.demoChat.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException e) {

        ErrorResponse error = new ErrorResponse(
                e.getField(),
                e.getMessage()
        );

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(error);
    }
}