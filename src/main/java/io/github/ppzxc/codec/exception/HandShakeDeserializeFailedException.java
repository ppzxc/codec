package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

/**
 * The type Hand shake deserialize failed exception.
 */
public class HandShakeDeserializeFailedException extends ProblemCodeException {

  private static final long serialVersionUID = -827932896260193369L;

  /**
   * Instantiates a new Hand shake deserialize failed exception.
   *
   * @param header the header
   */
  public HandShakeDeserializeFailedException(Header header) {
    super("handShake deserialize failed", header.getId(), ProblemCode.HANDSHAKE_DESERIALIZE_FAILED);
  }

  /**
   * Instantiates a new Hand shake deserialize failed exception.
   *
   * @param header    the header
   * @param throwable the throwable
   */
  public HandShakeDeserializeFailedException(Header header, Throwable throwable) {
    super("handShake deserialize failed", throwable, header.getId(), ProblemCode.HANDSHAKE_DESERIALIZE_FAILED);
  }

  /**
   * Instantiates a new Hand shake deserialize failed exception.
   *
   * @param id the id
   */
  public HandShakeDeserializeFailedException(int id) {
    super(id, ProblemCode.HANDSHAKE_DESERIALIZE_FAILED);
  }

  /**
   * Instantiates a new Hand shake deserialize failed exception.
   *
   * @param message the message
   */
  public HandShakeDeserializeFailedException(String message) {
    super(message, 0, ProblemCode.HANDSHAKE_DESERIALIZE_FAILED);
  }

  /**
   * Instantiates a new Hand shake deserialize failed exception.
   *
   * @param id      the id
   * @param message the message
   */
  public HandShakeDeserializeFailedException(int id, String message) {
    super(message, id, ProblemCode.HANDSHAKE_DESERIALIZE_FAILED);
  }
}
