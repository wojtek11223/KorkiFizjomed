package com.example.KorkiMedic.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleExecutionException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleSecurityException(Exception exception) {

        exception.printStackTrace();

        if (exception instanceof BadCredentialsException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The username or password is incorrect");
        }

        if (exception instanceof AccountStatusException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The account is locked");
        }

        if (exception instanceof AccessDeniedException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You are not authorized to access this resource");
        }

        if (exception instanceof SignatureException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The JWT signature is invalid");
        }

        if (exception instanceof ExpiredJwtException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Sesja się skończyła, proszę się wylogować i zalogować ponownie bo i tak nic nie zrobisz");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unknown internal server error.");

    }

}