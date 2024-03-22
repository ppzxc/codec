package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.CodecCode;

public class BlankBodyCodecException extends CodecException {

  private static final long serialVersionUID = 8254481768801292762L;

  public BlankBodyCodecException(String message) {
    super(message, DEFAULT_ID, REJECT_UNRECOGNIZED, INSTANCE_DECODER, new Object[]{}, CodecCode.BLANK_BODY);
  }
}
