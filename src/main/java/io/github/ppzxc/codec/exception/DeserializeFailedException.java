package io.github.ppzxc.codec.exception;

public class DeserializeFailedException extends ProblemCodeException {

  private static final long serialVersionUID = 465217152991183951L;

  public DeserializeFailedException(Throwable throwable) {
    super("deserialize failed", throwable, 0, ProblemCode.DESERIALIZE_FAILED);
  }
}
