package io.github.ppzxc.codec.exception;

public class SerializeFailedException extends ProblemCodeException {

  private static final long serialVersionUID = -4862359733551583748L;

  public SerializeFailedException(Throwable throwable) {
    super("serialize failed", throwable, 0, ProblemCode.SERIALIZE_FAILED);
  }
}
