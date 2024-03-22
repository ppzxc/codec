package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.CodecCode;

public class DecryptCodecException extends CodecException {

  private static final long serialVersionUID = 5455057470289902651L;

  public DecryptCodecException(String rejectedValue, CodecCode codecCode) {
    super("rejected: " + rejectedValue, DEFAULT_ID, rejectedValue, INSTANCE_HANDSHAKE, new Object[]{rejectedValue}, codecCode);
  }

  public DecryptCodecException(int rejectedValue, CodecCode codecCode) {
    this(String.valueOf(rejectedValue), codecCode);
  }

  public DecryptCodecException(byte rejectedValue, CodecCode codecCode) {
    this(String.format("0x%02x", rejectedValue), codecCode);
  }

  public DecryptCodecException(Throwable cause, CodecCode codecCode) {
    super(cause, DEFAULT_ID, DEFAULT_REJECT, INSTANCE_HANDSHAKE, new Object[]{}, codecCode);
  }
}
