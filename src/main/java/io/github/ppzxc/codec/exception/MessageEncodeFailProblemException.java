package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

/**
 * The type Message encode fail problem exception.
 */
public class MessageEncodeFailProblemException extends CodecProblemException {

  private static final long serialVersionUID = -2797875406038916760L;

  /**
   * Instantiates a new Message encode fail problem exception.
   *
   * @param header    the header
   * @param throwable the throwable
   */
  public MessageEncodeFailProblemException(Header header, Throwable throwable) {
    super("outbound message encrypt failed", throwable, header.getId(), "[UNRECOGNIZED]", "/encoder", new Object[]{},
      CodecProblem.OUTBOUND_MESSAGE_ENCODE_FAILED);
  }
}
