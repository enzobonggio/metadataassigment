package io.metadata.students.adapters.input.rest;

import io.metadata.students.adapters.output.persistence.exception.StudentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler
{

    @ExceptionHandler(value = StudentNotFoundException.class)
    public ResponseEntity<StudentNotFoundException> handle(StudentNotFoundException e)
    {
        return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
    }
}
