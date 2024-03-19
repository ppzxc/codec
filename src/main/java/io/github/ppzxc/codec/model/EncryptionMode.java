package io.github.ppzxc.codec.model;

import java.util.Arrays;

public enum EncryptionMode {
  NONE((byte) 0x00),
  ELECTRONIC_CODE_BLOCK((byte) 0x01),
  CIPHER_BLOCK_CHAINING((byte) 0x02),
  CIPHER_FEEDBACK((byte) 0x03),
  OUTPUT_FEEDBACK((byte) 0x04),
  COUNTER((byte) 0x05);

  private final byte code;

  EncryptionMode(byte code) {
    this.code = code;
  }

  public byte getCode() {
    return code;
  }

  public static EncryptionMode of(byte value) {
    return Arrays.stream(EncryptionMode.values())
      .filter(status -> status.code == value)
      .findAny()
      .orElse(EncryptionMode.NONE);
  }

  @Override
  public String toString() {
    return "EncryptionMode." + name() + "(" + code + ")";
  }
}
