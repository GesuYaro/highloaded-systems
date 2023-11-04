package com.dedlam.ftesterlab.utils.exceptions;

public class MyException extends RuntimeException {
  public MyException() {
  }

  public MyException(String message) {
    super(message);
  }

  public MyException(String message, Throwable cause) {
    super(message, cause);
  }
}
