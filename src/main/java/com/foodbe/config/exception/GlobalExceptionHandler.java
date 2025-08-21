package com.foodbe.config.exception;

import com.foodbe.DTO.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(RuntimeException ex) {
        ApiResponse<Void> response =
                ApiResponse.buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = "Validation failed";
        if (!ex.getBindingResult().getAllErrors().isEmpty()) {
            if (ex.getBindingResult().getFieldError() != null) {
                FieldError fe = ex.getBindingResult().getFieldError();
                message = fe.getDefaultMessage();
            } else {
                message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
            }
        }
        ApiResponse<Void> response = ApiResponse.buildErrorResponse(HttpStatus.BAD_REQUEST, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 404 cho request không khớp bất kỳ handler nào
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoHandler(NoHandlerFoundException ex, HttpServletRequest req) {
        ApiResponse<Void> response =
                ApiResponse.buildErrorResponse(HttpStatus.NOT_FOUND, "Not Found: " + req.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // 405 Method Not Allowed
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        ApiResponse<Void> response =
                ApiResponse.buildErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    // 400 khi JSON body không hợp lệ
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadJson(HttpMessageNotReadableException ex) {
        ApiResponse<Void> response =
                ApiResponse.buildErrorResponse(HttpStatus.BAD_REQUEST, "Malformed JSON request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleServerError(Exception ex) {
        ApiResponse<Void> response =
                ApiResponse.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Internal Server Error: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
