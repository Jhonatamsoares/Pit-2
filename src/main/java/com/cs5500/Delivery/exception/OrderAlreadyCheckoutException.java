package com.cs5500.Delivery.exception;

public class OrderAlreadyCheckoutException extends Exception {

  public OrderAlreadyCheckoutException(String message) {
    super(message);
  }
}
