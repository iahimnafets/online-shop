package com.online.shop.exception;

import com.online.shop.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@ControllerAdvice(annotations = RestController.class)
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler( value = {ApiRequestException.class} )
    public final ResponseEntity<Object> handleApiRequestException(ApiRequestException e){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        Response responseEx = Response.builder()
                .status(badRequest)
                .statusCode(badRequest.value())
                .message( e.getMessage() )
                .timeStamp( LocalDateTime.now() )
                .build();

        return new ResponseEntity<>(responseEx,
                badRequest);
    }

    @ExceptionHandler ( Exception.class )
    public final ResponseEntity<Object> handleAllExceptions( Exception e )
    {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        Response responseEx = Response.builder()
                .status(badRequest)
                .statusCode(badRequest.value())
                .message( e.getMessage() )
                .timeStamp( LocalDateTime.now() )
                .build();

        log.error( "Error", e);

        return new ResponseEntity<>(responseEx,
                badRequest);
    }
}
