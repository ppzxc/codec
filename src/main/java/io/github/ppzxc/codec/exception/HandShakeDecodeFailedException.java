package io.github.ppzxc.codec.exception;

public class HandShakeDecodeFailedException extends CodecProblemException {

  private static final long serialVersionUID = -6050594418941058551L;

  public HandShakeDecodeFailedException(int id, Throwable cause) {
    super(cause, id, "[UNRECOGNIZED]", "/handshake", new Object[]{}, CodecProblem.HANDSHAKE_DECODE_FAIL);
  }
}
