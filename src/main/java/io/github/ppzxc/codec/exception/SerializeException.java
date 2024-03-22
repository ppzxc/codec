package io.github.ppzxc.codec.exception;

public class SerializeException extends Exception {

  private static final long serialVersionUID = -4862359733551583748L;

  public SerializeException() {
    super("serialize failed");
  }

  public SerializeException(String message) {
    super(message);
  }

  public SerializeException(Throwable throwable) {
    super("serialize failed", throwable);
  }
}
