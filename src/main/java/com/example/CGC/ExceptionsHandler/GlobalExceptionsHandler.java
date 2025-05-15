package com.example.CGC.ExceptionsHandler;

import com.example.CGC.CGCExceptions.CGCExceptions;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionsHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> unhandledExceptions(
            MethodArgumentNotValidException methodArgumentNotValidException
    ){
        HashMap<String , Object> response = new HashMap<>();
        ArrayList<String> errors = new ArrayList<>();

        for(int i = 0; i < methodArgumentNotValidException.getAllErrors().size(); i++ ){
            String fieldName = methodArgumentNotValidException.getFieldErrors().get(i).getField();
            String message = methodArgumentNotValidException.getAllErrors().get(i).getDefaultMessage();
            errors.add(message);
        }

        response.put("timestamp",  LocalDateTime.now());
        response.put("schema", errors);

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(400));
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<?> unhandledGenericExceptions(
            Exception exception
    ){
        HashMap<String , Object> response = new HashMap<>();
        response.put("timestamp",  LocalDateTime.now());
        response.put("error", exception.getMessage());

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(400));
    }

    @ExceptionHandler({CGCExceptions.class})
    public ResponseEntity<?> handleCGCExceptions(
            CGCExceptions exception
    ){
      return exception.getErrorMessage();
    }


}
