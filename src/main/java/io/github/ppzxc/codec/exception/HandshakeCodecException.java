package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.CodecCode;

public class HandshakeCodecException extends CodecException {

  private static final long serialVersionUID = -8805202120396420424L;

  public HandshakeCodecException(String rejectedValue, CodecCode codecCode) {
    super("rejected: " + rejectedValue, DEFAULT_ID, rejectedValue, INSTANCE_HANDSHAKE, new Object[]{rejectedValue}, codecCode);
  }

  public HandshakeCodecException(int rejectedValue, CodecCode codecCode) {
    this(String.valueOf(rejectedValue), codecCode);
  }

  public HandshakeCodecException(byte rejectedValue, CodecCode codecCode) {
    this(String.format("0x%02x", rejectedValue), codecCode);
  }

  public HandshakeCodecException(Throwable cause, CodecCode codecCode) {
    super(cause, DEFAULT_ID, REJECT_UNRECOGNIZED, INSTANCE_HANDSHAKE, new Object[]{}, codecCode);
  }
}
