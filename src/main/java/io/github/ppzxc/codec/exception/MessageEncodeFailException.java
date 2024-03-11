package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

/**
 * The type Message encode fail exception.
 */
public class MessageEncodeFailException extends ProblemCodeException {

  private static final long serialVersionUID = -2797875406038916760L;

  /**
   * Instantiates a new Message encode fail exception.
   *
   * @param header the header
   * @param throwable the throwable
   */
  public MessageEncodeFailException(Header header, Throwable throwable) {
    super("outbound message encrypt failed", throwable, header.getId(), ProblemCode.OUTBOUND_MESSAGE_ENCRYPT_FAILED);
  }
}
