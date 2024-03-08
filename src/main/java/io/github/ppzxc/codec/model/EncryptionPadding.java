package io.github.ppzxc.codec.model;

import java.util.Arrays;

public enum EncryptionPadding {
  NULL("null"),
  PKCS5PADDING("PKCS5Padding"),
  PKCS7PADDING("PKCS7Padding");

  private final String code;

  EncryptionPadding(String code) {
    this.code = code;
  }

  public static EncryptionPadding of(String value) {
    return Arrays.stream(EncryptionPadding.values())
      .filter(status -> status.code.equalsIgnoreCase(value))
      .findAny()
      .orElse(EncryptionPadding.NULL);
  }
}
