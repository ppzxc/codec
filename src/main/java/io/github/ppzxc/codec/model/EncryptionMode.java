package io.github.ppzxc.codec.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

public enum EncryptionMode {
  NONE("NONE"),
  ELECTRONIC_CODE_BLOCK("ECB"),
  CIPHER_BLOCK_CHAINING("CBC"),
  CIPHER_FEEDBACK("CFB"),
  OUTPUT_FEEDBACK("OFB"),
  COUNTER("CTR");

  private final String code;

  EncryptionMode(String code) {
    this.code = code;
  }

  @JsonValue
  public String getCode() {
    return code;
  }

  @JsonCreator
  public static EncryptionMode of(String value) {
    return Arrays.stream(EncryptionMode.values())
      .filter(status -> status.code.equalsIgnoreCase(value))
      .findAny()
      .orElse(EncryptionMode.NONE);
  }
}
