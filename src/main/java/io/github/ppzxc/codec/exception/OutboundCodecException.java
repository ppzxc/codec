package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.CodecCode;
import io.github.ppzxc.codec.model.Header;

public class OutboundCodecException extends CodecException {

  private static final long serialVersionUID = -2797875406038916760L;

  public OutboundCodecException(Header header, Throwable throwable) {
    super("outbound message encrypt failed", throwable, header.getId(), DEFAULT_REJECT, INSTANCE_ENCODER, new Object[]{},
      CodecCode.ENCODE_FAIL);
  }
}
