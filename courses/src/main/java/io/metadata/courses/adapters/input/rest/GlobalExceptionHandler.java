package io.metadata.courses.adapters.input.rest;

import io.metadata.courses.adapters.output.persistence.exception.CourseNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler
{

    @ExceptionHandler(value = CourseNotFoundException.class)
    public ResponseEntity<CourseNotFoundException> handle(CourseNotFoundException e)
    {
        return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
    }
}
