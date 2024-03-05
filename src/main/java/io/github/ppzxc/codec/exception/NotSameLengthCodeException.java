package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

public class NotSameLengthCodeException extends ProblemCodeException {

  private static final long serialVersionUID = 1186906307527013515L;

  public NotSameLengthCodeException(Header header, int readableBytesBody) {
    super(String.format("not same length: %d != %d", header.getBodyLength(), readableBytesBody), header.getId(),
      ProblemCode.NOT_SAME_LENGTH);
  }

  public NotSameLengthCodeException(int id) {
    super(id, ProblemCode.NOT_SAME_LENGTH);
  }

  public NotSameLengthCodeException(String message) {
    super(message, 0, ProblemCode.NOT_SAME_LENGTH);
  }

  public NotSameLengthCodeException(int id, String message) {
    super(message, id, ProblemCode.NOT_SAME_LENGTH);
  }
}
