package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

public class HandShakeDecodeFailException extends ProblemCodeException {

  private static final long serialVersionUID = -6050594418941058551L;

  public HandShakeDecodeFailException(Header header) {
    super("handShake decode fail", header.getId(), ProblemCode.HANDSHAKE_DECODE_FAIL);
  }

  public HandShakeDecodeFailException(Header header, Throwable throwable) {
    super("handShake decode fail", throwable, header.getId(), ProblemCode.HANDSHAKE_DECODE_FAIL);
  }

  public HandShakeDecodeFailException(int id) {
    super(id, ProblemCode.HANDSHAKE_DECODE_FAIL);
  }

  public HandShakeDecodeFailException(String message) {
    super(message, 0, ProblemCode.HANDSHAKE_DECODE_FAIL);
  }

  public HandShakeDecodeFailException(int id, String message) {
    super(message, id, ProblemCode.HANDSHAKE_DECODE_FAIL);
  }
}
