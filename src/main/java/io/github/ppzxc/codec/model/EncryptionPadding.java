package io.github.ppzxc.codec.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

/**
 * The enum Encryption padding.
 */
public enum EncryptionPadding {
  /**
   * None encryption padding.
   */
  NONE("NONE"),
  /**
   * Pkcs 5 padding encryption padding.
   */
  PKCS5PADDING("PKCS5Padding"),
  /**
   * Pkcs 7 padding encryption padding.
   */
  PKCS7PADDING("PKCS7Padding");

  private final String code;

  EncryptionPadding(String code) {
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
   * Of encryption padding.
   *
   * @param value the value
   * @return the encryption padding
   */
  @JsonCreator
  public static EncryptionPadding of(String value) {
    return Arrays.stream(EncryptionPadding.values())
      .filter(status -> status.code.equalsIgnoreCase(value))
      .findAny()
      .orElse(ofName(value));
  }

  /**
   * Of name encryption padding.
   *
   * @param value the value
   * @return the encryption padding
   */
  public static EncryptionPadding ofName(String value) {
    return Arrays.stream(EncryptionPadding.values())
      .filter(status -> status.name().equalsIgnoreCase(value))
      .findAny()
      .orElse(EncryptionPadding.NONE);
  }
}
