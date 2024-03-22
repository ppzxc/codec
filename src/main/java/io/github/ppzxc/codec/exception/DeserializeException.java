package io.github.ppzxc.codec.exception;

public class DeserializeException extends Exception {

  private static final long serialVersionUID = 465217152991183951L;

  public DeserializeException() {
    super("deserialize failed");
  }

  public DeserializeException(String message) {
    super(message);
  }

  public DeserializeException(Throwable throwable) {
    super("deserialize failed", throwable);
  }
}
