package com.github.youssefagagg.automaticirrigationsystem.exception;

public class NoMoreThanOneSensorAllowed extends
    RuntimeException {

  public NoMoreThanOneSensorAllowed(String message) {
    super(message);
  }
}
