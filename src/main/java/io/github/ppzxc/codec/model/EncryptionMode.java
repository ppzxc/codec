package io.github.ppzxc.codec.model;

import java.util.Arrays;

public enum EncryptionMode {
  NULL("null"),
  ELECTRONIC_CODE_BLOCK("ECB"),
  CIPHER_BLOCK_CHAINING("CBC"),
  CIPHER_FEEDBACK("CFB"),
  OUTPUT_FEEDBACK("OFB"),
  COUNTER("CTR");

  private final String code;

  EncryptionMode(String code) {
    this.code = code;
  }

  public static EncryptionMode of(String value) {
    return Arrays.stream(EncryptionMode.values())
      .filter(status -> status.code.equalsIgnoreCase(value))
      .findAny()
      .orElse(EncryptionMode.NULL);
  }
}
