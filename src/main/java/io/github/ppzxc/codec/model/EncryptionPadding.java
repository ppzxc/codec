package io.github.ppzxc.codec.model;

import java.util.Arrays;

public enum EncryptionPadding {
  NONE((byte) 0x00),
  PKCS5PADDING((byte) 0x01),
  PKCS7PADDING((byte) 0x02);

  private final byte code;

  EncryptionPadding(byte code) {
    this.code = code;
  }

  public byte getCode() {
    return code;
  }

  public static EncryptionPadding of(byte value) {
    return Arrays.stream(EncryptionPadding.values())
      .filter(status -> status.code == value)
      .findAny()
      .orElse(EncryptionPadding.NONE);
  }

  @Override
  public String toString() {
    return "EncryptionPadding." + name() + "(" + code + ")";
  }
}
