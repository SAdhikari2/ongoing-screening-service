package com.saikat.fenergo.screening.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<String>> handle(Exception ex) {
        return Mono.just(
                ResponseEntity.internalServerError()
                        .body(ex.getMessage())
        );
    }
}
