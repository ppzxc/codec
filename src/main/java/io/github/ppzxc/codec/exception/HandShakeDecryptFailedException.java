package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

/**
 * The type Hand shake decrypt failed exception.
 */
public class HandShakeDecryptFailedException extends ProblemCodeException {

  private static final long serialVersionUID = -4655458165601131794L;

  /**
   * Instantiates a new Hand shake decrypt failed exception.
   *
   * @param header the header
   */
  public HandShakeDecryptFailedException(Header header) {
    super("handShake decrypt failed", header.getId(), ProblemCode.HANDSHAKE_DECRYPT_FAILED);
  }

  /**
   * Instantiates a new Hand shake decrypt failed exception.
   *
   * @param header    the header
   * @param throwable the throwable
   */
  public HandShakeDecryptFailedException(Header header, Throwable throwable) {
    super("handShake decrypt failed", throwable, header.getId(), ProblemCode.HANDSHAKE_DECRYPT_FAILED);
  }

  /**
   * Instantiates a new Hand shake decrypt failed exception.
   *
   * @param id the id
   */
  public HandShakeDecryptFailedException(int id) {
    super(id, ProblemCode.HANDSHAKE_DECRYPT_FAILED);
  }

  /**
   * Instantiates a new Hand shake decrypt failed exception.
   *
   * @param message the message
   */
  public HandShakeDecryptFailedException(String message) {
    super(message, 0, ProblemCode.HANDSHAKE_DECRYPT_FAILED);
  }

  /**
   * Instantiates a new Hand shake decrypt failed exception.
   *
   * @param id      the id
   * @param message the message
   */
  public HandShakeDecryptFailedException(int id, String message) {
    super(message, id, ProblemCode.HANDSHAKE_DECRYPT_FAILED);
  }
}
