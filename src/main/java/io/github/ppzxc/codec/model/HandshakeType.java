package io.github.ppzxc.codec.model;

import java.util.Arrays;

public enum HandshakeType {
  NONE((byte) 0x00),
  RSA_1024((byte) 0x01),
  RSA_2048((byte) 0x02),
  RSA_4096((byte) 0x03);

  private final byte code;

  HandshakeType(byte code) {
    this.code = code;
  }

  public byte getCode() {
    return code;
  }

  public static HandshakeType of(byte value) {
    return Arrays.stream(HandshakeType.values())
      .filter(handShakeType -> handShakeType.code == value)
      .findAny()
      .orElse(HandshakeType.NONE);
  }
}
