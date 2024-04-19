package com.atp.bdss.exceptions;

import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.utils.ErrorsApp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ResponseData> handleRuntimeException(RuntimeException exception){

        ResponseData responseData = new ResponseData();
        responseData.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        responseData.setMessage("Internal Server Error");
        responseData.setData(exception.getMessage());

        return ResponseEntity.status(
                        HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseData);
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


    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ResponseData> handleAccessDeniedException(AccessDeniedException exception){

        ErrorsApp errorsApp = ErrorsApp.UNAUTHORIZED;

        ResponseData responseData = new ResponseData();
        responseData.setCode(errorsApp.getStatusCode().value());
        responseData.setMessage("unauthorized");
        responseData.setData(errorsApp.getDescription());

        return ResponseEntity
                .status(errorsApp.getStatusCode())
                .body(responseData);
    }

    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<ResponseData> handleIOException(IOException exception){

        ResponseData responseData = new ResponseData();
        responseData.setCode(HttpStatus.BAD_REQUEST.value());
        responseData.setMessage("Error");
        responseData.setData(exception.getMessage());

        return ResponseEntity.status(
                        HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseData);
    }
}
