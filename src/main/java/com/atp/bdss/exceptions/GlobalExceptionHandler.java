package com.atp.bdss.exceptions;

import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.utils.ErrorsApp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ResponseData> handlingRuntimeException(RuntimeException exception) {
        log.error("Exception: ", exception);
        ResponseData responseData = new ResponseData();

        responseData.setCode(ErrorsApp.UNCATEGORIZED_EXCEPTION.getCode());
        responseData.setMessage(ErrorsApp.UNCATEGORIZED_EXCEPTION.getDescription());
        return ResponseEntity.badRequest().body(responseData);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseData> handleValidationException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ResponseData responseData = new ResponseData();
        responseData.setCode(HttpStatus.BAD_REQUEST.value());
        responseData.setMessage("Validation Error");
        responseData.setData(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
    }

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ResponseData> handleAppException(CustomException exception){

        ErrorsApp errorsApp = exception.getErrorApp();

        ResponseData responseData = new ResponseData();
        responseData.setCode(errorsApp.getCode());
        responseData.setMessage("Bad request");
        responseData.setData(errorsApp.getDescription());

        return ResponseEntity
                .status(errorsApp.getStatusCode())
                .body(responseData);
    }


    @ExceptionHandler(value = org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ResponseData> handleAccessDeniedException(AccessDeniedException exception){

        ErrorsApp errorsApp = ErrorsApp.UNAUTHORIZED;

        ResponseData responseData = new ResponseData();
        responseData.setCode(errorsApp.getCode());
        responseData.setMessage(errorsApp.getDescription());
        responseData.setData(exception.getMessage());

        return ResponseEntity
                .status(errorsApp.getStatusCode())
                .body(responseData);
    }

    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<ResponseData> handleIOException(IOException exception){

        ResponseData responseData = new ResponseData();
        responseData.setCode(HttpStatus.BAD_REQUEST.value());
        responseData.setMessage("Error upload image");
        responseData.setData(exception.getMessage());

        return ResponseEntity.status(
                        HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseData);
    }

    @ExceptionHandler(value = AuthenticationServiceException.class)
    public ResponseEntity<ResponseData> handleAuthenticationServiceException(AuthenticationServiceException exception){

        ErrorsApp errorsApp = ErrorsApp.UNAUTHENTICATED;

        ResponseData responseData = new ResponseData();
        responseData.setCode(errorsApp.getCode());
        responseData.setMessage(errorsApp.getDescription());
        responseData.setData(exception.getMessage());

        return ResponseEntity
                .status(errorsApp.getStatusCode())
                .body(responseData);
    }
}
