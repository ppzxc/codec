package io.github.ppzxc.codec.model;

import java.util.Arrays;

public enum EncryptionType {
  NULL("null"),
  ADVANCED_ENCRYPTION_STANDARD("AES");

  private final String code;

  EncryptionType(String code) {
    this.code = code;
  }

  public static EncryptionType of(String value) {
    return Arrays.stream(EncryptionType.values())
      .filter(status -> status.code.equalsIgnoreCase(value))
      .findAny()
      .orElse(EncryptionType.NULL);
  }
}
