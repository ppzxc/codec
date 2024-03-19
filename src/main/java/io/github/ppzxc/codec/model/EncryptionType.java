package io.github.ppzxc.codec.model;

import java.util.Arrays;

public enum EncryptionType {
  NONE((byte) 0x00),
  ADVANCED_ENCRYPTION_STANDARD((byte) 0x01);

  private final byte code;

  EncryptionType(byte code) {
    this.code = code;
  }

  public byte getCode() {
    return code;
  }

  public static EncryptionType of(byte value) {
    return Arrays.stream(EncryptionType.values())
      .filter(status -> status.code == value)
      .findAny()
      .orElse(EncryptionType.NONE);
  }

  @Override
  public String toString() {
    return "EncryptionType." + name() + "(" + code + ")";
  }
}
