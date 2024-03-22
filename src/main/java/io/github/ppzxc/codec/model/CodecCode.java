package io.github.ppzxc.codec.model;

import java.util.Arrays;

public enum CodecCode {
  NONE((byte) 0x00),
  OK((byte) 0x01),
  UNRECOGNIZED((byte) 0x02),
  SHORT_LENGTH((byte) 0x03),
  SHORT_LENGTH_FIELD((byte) 0x04),
  INVALID_HAND_SHAKE_TYPE((byte) 0x05),
  INVALID_ENCRYPTION_TYPE((byte) 0x06),
  INVALID_ENCRYPTION_MODE((byte) 0x07),
  INVALID_ENCRYPTION_PADDING((byte) 0x08),
  DECRYPT_FAIL((byte) 0x09),
  CRYPTO_CREATE_FAIL((byte) 0x0a),
  INVALID_KEY_SIZE((byte) 0x0b),
  BLANK_BODY((byte) 0x0c),
  ENCODE_FAIL((byte) 0x0d),
  LENGTH_NOT_EQUALS_READABLE((byte) 0x0e),
  MISSING_LINE_DELIMITER((byte) 0x0f),
  HANDSHAKE_TIMEOUT_NO_BEHAVIOR((byte) 0x10),
  HANDSHAKE_TIMEOUT_NO_INCOMING((byte) 0x11),
  HANDSHAKE_TIMEOUT_NO_OUTGOING((byte) 0x12),
  ;

  private final byte code;

  CodecCode(byte code) {
    this.code = code;
  }

  public byte getCode() {
    return code;
  }

  public static CodecCode of(byte value) {
    return Arrays.stream(CodecCode.values())
      .filter(code -> code.code == value)
      .findAny()
      .orElse(CodecCode.NONE);
  }

  @Override
  public String toString() {
    return "CodecCode." + name() + "(" + code + ")";
  }
}
