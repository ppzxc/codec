package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.CodecProblemCode;
import io.github.ppzxc.codec.model.Header;

public class OutboundMessageEncoderException extends CodecProblemException {

  private static final long serialVersionUID = -2797875406038916760L;

  public OutboundMessageEncoderException(Header header, Throwable throwable) {
    super("outbound message encrypt failed", throwable, header.getId(), "[UNRECOGNIZED]", "/encoder", new Object[]{},
      CodecProblemCode.ENCODE_FAIL);
  }
}
