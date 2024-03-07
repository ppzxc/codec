package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

/**
 * The type Hand shake decode fail exception.
 */
public class HandShakeDecodeFailException extends ProblemCodeException {

  private static final long serialVersionUID = -6050594418941058551L;

  /**
   * Instantiates a new Hand shake decode fail exception.
   *
   * @param header    the header
   * @param throwable the throwable
   */
  public HandShakeDecodeFailException(Header header, Throwable throwable) {
    super("handShake decode fail", throwable, header.getId(), ProblemCode.HANDSHAKE_DECODE_FAIL);
  }
}
