package io.metadata.subscriptions.adapters.input.rest;

import io.metadata.subscriptions.domain.services.exception.CourseNotFoundException;
import io.metadata.subscriptions.domain.services.exception.StudentNotFoundException;
import io.metadata.subscriptions.domain.services.exception.SubscriptionNotAllowedException;
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

    @ExceptionHandler(value = StudentNotFoundException.class)
    public ResponseEntity<StudentNotFoundException> handle(StudentNotFoundException e)
    {
        return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = SubscriptionNotAllowedException.class)
    public ResponseEntity<SubscriptionNotAllowedException> handle(SubscriptionNotAllowedException e)
    {
        return new ResponseEntity<>(e, HttpStatus.CONFLICT);
    }
}
