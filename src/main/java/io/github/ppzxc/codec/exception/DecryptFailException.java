package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.CodecProblemCode;

public class DecryptFailException extends CodecProblemException {

  private static final long serialVersionUID = 5455057470289902651L;

  public DecryptFailException(String rejectedValue, CodecProblemCode codecProblemCode) {
    super("rejected: " + rejectedValue, 0L, rejectedValue, "/handshake", new Object[]{rejectedValue}, codecProblemCode);
  }

  public DecryptFailException(int rejectedValue, CodecProblemCode codecProblemCode) {
    this(String.valueOf(rejectedValue), codecProblemCode);
  }

  public DecryptFailException(byte rejectedValue, CodecProblemCode codecProblemCode) {
    this(String.format("0x%02x", rejectedValue), codecProblemCode);
  }

  public DecryptFailException(Throwable cause, CodecProblemCode codecProblemCode) {
    super(cause, 0L, "[UNRECOGNIZED]", "/handshake", new Object[]{}, codecProblemCode);
  }
}
