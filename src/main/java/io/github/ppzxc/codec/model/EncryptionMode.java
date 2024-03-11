package io.github.ppzxc.codec.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

/**
 * The enum Encryption mode.
 */
public enum EncryptionMode {
  /**
   * None encryption mode.
   */
  NONE("NONE"),
  /**
   * Electronic code block encryption mode.
   */
  ELECTRONIC_CODE_BLOCK("ECB"),
  /**
   * Cipher block chaining encryption mode.
   */
  CIPHER_BLOCK_CHAINING("CBC"),
  /**
   * Cipher feedback encryption mode.
   */
  CIPHER_FEEDBACK("CFB"),
  /**
   * Output feedback encryption mode.
   */
  OUTPUT_FEEDBACK("OFB"),
  /**
   * Counter encryption mode.
   */
  COUNTER("CTR");

  private final String code;

  EncryptionMode(String code) {
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
   * Of encryption mode.
   *
   * @param value the value
   * @return the encryption mode
   */
  @JsonCreator
  public static EncryptionMode of(String value) {
    return Arrays.stream(EncryptionMode.values())
      .filter(status -> status.code.equalsIgnoreCase(value))
      .findAny()
      .orElse(ofName(value));
  }

  /**
   * Of name encryption mode.
   *
   * @param value the value
   * @return the encryption mode
   */
  public static EncryptionMode ofName(String value) {
    return Arrays.stream(EncryptionMode.values())
      .filter(status -> status.name().equalsIgnoreCase(value))
      .findAny()
      .orElse(EncryptionMode.NONE);
  }
}
