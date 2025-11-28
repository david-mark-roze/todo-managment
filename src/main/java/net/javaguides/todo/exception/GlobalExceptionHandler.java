package net.javaguides.todo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

// Notifies controllers that this a central exception handler
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TodoAPIException.class)
    public ResponseEntity<ErrorDetails> handleTodoAPIException(TodoAPIException exception, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                // The 'false' parameter specifies that only the URL is returned
                // from the web request and not other details.
                webRequest.getDescription(false));
        return new ResponseEntity<ErrorDetails>(errorDetails, exception.getHttpStatus());
    }
}
