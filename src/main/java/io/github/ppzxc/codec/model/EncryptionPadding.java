package io.github.ppzxc.codec.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

public enum EncryptionPadding {
  NONE("NONE"),
  PKCS5PADDING("PKCS5Padding"),
  PKCS7PADDING("PKCS7Padding");

  private final String code;

  EncryptionPadding(String code) {
    this.code = code;
  }

  @JsonValue
  public String getCode() {
    return code;
  }

  @JsonCreator
  public static EncryptionPadding of(String value) {
    return Arrays.stream(EncryptionPadding.values())
      .filter(status -> status.code.equalsIgnoreCase(value))
      .findAny()
      .orElse(ofName(value));
  }

  public static EncryptionPadding ofName(String value) {
    return Arrays.stream(EncryptionPadding.values())
      .filter(status -> status.name().equalsIgnoreCase(value))
      .findAny()
      .orElse(EncryptionPadding.NONE);
  }
}
