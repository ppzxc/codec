package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

public class NotEqualLengthException extends CodecProblemException {

  private static final long serialVersionUID = 1186906307527013515L;

  public NotEqualLengthException(Header header, String rejectedValue) {
    super(String.format("not same length: %d != %s", header.getBodyLength(), rejectedValue), header.getId(),
      rejectedValue, "/decoder", new Object[]{rejectedValue}, CodecProblem.NOT_SAME_LENGTH);
  }
}
