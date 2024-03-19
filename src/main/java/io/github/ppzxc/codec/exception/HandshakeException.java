package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.CodecProblemCode;

public class HandshakeException extends CodecProblemException {

  private static final long serialVersionUID = -8805202120396420424L;

  public HandshakeException(String rejectedValue, CodecProblemCode codecProblemCode) {
    super("rejected: " + rejectedValue, 0L, rejectedValue, "/handshake", new Object[]{rejectedValue}, codecProblemCode);
  }

  public HandshakeException(int rejectedValue, CodecProblemCode codecProblemCode) {
    this(String.valueOf(rejectedValue), codecProblemCode);
  }

  public HandshakeException(byte rejectedValue, CodecProblemCode codecProblemCode) {
    this(String.format("0x%02x", rejectedValue), codecProblemCode);
  }

  public HandshakeException(Throwable cause, CodecProblemCode codecProblemCode) {
    super(cause, 0L, "[UNRECOGNIZED]", "/handshake", new Object[]{}, codecProblemCode);
  }
}
