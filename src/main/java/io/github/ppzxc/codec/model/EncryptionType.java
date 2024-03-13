package io.github.ppzxc.codec.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

public enum EncryptionType {
  NONE("NONE"),
  ADVANCED_ENCRYPTION_STANDARD("AES");

  private final String code;

  EncryptionType(String code) {
    this.code = code;
  }

  @JsonValue
  public String getCode() {
    return code;
  }

  @JsonCreator
  public static EncryptionType of(String value) {
    return Arrays.stream(EncryptionType.values())
      .filter(status -> status.code.equalsIgnoreCase(value))
      .findAny()
      .orElse(ofName(value));
  }

  public static EncryptionType ofName(String value) {
    return Arrays.stream(EncryptionType.values())
      .filter(status -> status.name().equalsIgnoreCase(value))
      .findAny()
      .orElse(EncryptionType.NONE);
  }
}
