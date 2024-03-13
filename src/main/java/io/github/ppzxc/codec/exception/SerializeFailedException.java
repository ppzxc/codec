package io.github.ppzxc.codec.exception;

public class SerializeFailedException extends Exception {

  private static final long serialVersionUID = -4862359733551583748L;

  public SerializeFailedException() {
    super("serialize failed");
  }

  public SerializeFailedException(String message) {
    super(message);
  }

  public SerializeFailedException(Throwable throwable) {
    super("serialize failed", throwable);
  }
}
