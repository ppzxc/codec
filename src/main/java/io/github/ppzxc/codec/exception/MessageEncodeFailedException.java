package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

public class MessageEncodeFailedException extends CodecProblemException {

  private static final long serialVersionUID = -2797875406038916760L;

  public MessageEncodeFailedException(Header header, Throwable throwable) {
    super("outbound message encrypt failed", throwable, header.getId(), "[UNRECOGNIZED]", "/encoder", new Object[]{},
      CodecProblem.OUTBOUND_MESSAGE_ENCODE_FAILED);
  }
}
