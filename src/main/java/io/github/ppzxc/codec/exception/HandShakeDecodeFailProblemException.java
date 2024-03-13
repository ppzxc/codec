package io.github.ppzxc.codec.exception;

/**
 * The type Hand shake decode fail problem exception.
 */
public class HandShakeDecodeFailProblemException extends CodecProblemException {

  private static final long serialVersionUID = -6050594418941058551L;

  /**
   * Instantiates a new Hand shake decode fail problem exception.
   *
   * @param id    the id
   * @param cause the cause
   */
  public HandShakeDecodeFailProblemException(int id, Throwable cause) {
    super(cause, id, "[UNRECOGNIZED]", "/handshake", new Object[]{}, CodecProblem.HANDSHAKE_DECODE_FAIL);
  }
}
