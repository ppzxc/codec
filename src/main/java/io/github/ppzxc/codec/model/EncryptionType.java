package io.github.ppzxc.codec.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

/**
 * The enum Encryption type.
 */
public enum EncryptionType {
  /**
   * None encryption type.
   */
  NONE("NONE"),
  /**
   * Advanced encryption standard encryption type.
   */
  ADVANCED_ENCRYPTION_STANDARD("AES");

  private final String code;

  EncryptionType(String code) {
    this.code = code;
  }

  /**
   * Gets code.
   *
   * @return the code
   */
  @JsonValue
  public String getCode() {
    return code;
  }

  /**
   * Of encryption type.
   *
   * @param value the value
   * @return the encryption type
   */
  @JsonCreator
  public static EncryptionType of(String value) {
    return Arrays.stream(EncryptionType.values())
      .filter(status -> status.code.equalsIgnoreCase(value))
      .findAny()
      .orElse(ofName(value));
  }

  /**
   * Of name encryption type.
   *
   * @param value the value
   * @return the encryption type
   */
  public static EncryptionType ofName(String value) {
    return Arrays.stream(EncryptionType.values())
      .filter(status -> status.name().equalsIgnoreCase(value))
      .findAny()
      .orElse(EncryptionType.NONE);
  }
}
