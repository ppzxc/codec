package io.github.ppzxc.codec.exception;

public class DeserializeFailedException extends Exception {

  private static final long serialVersionUID = 465217152991183951L;

  public DeserializeFailedException() {
    super("deserialize failed");
  }

  public DeserializeFailedException(String message) {
    super(message);
  }

  public DeserializeFailedException(Throwable throwable) {
    super("deserialize failed", throwable);
  }
}
