package com.github.youssefagagg.automaticirrigationsystem.web.advice;

import com.github.youssefagagg.automaticirrigationsystem.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
/**
 * A global exception handler for REST API.
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler {

  @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class,
      NoMoreThanOneSensorAllowed.class, SensorCantBeReachedException.class,
      PlotHasAlreadyStartedToBeIrrigated.class})
  protected ResponseEntity<Object> handleConflict(RuntimeException ex) {

    return new ResponseEntity<>( ex.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Object> resourceDoesntExistExceptionHandler(Exception ex) {

    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BadRequestAlertException.class)
  public ResponseEntity<Object> badRequestExceptionHandler(Exception ex) {

    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> defaultExceptionHandler(final Exception ex) {

    return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

  }
}
