package io.github.ppzxc.codec.model;

import java.util.Arrays;

/**
 * The enum Encoding type.
 */
public enum EncodingType {
  /**
   * Null encoding type.
   */
  NULL((byte) 0x00),
  /**
   * Protobuf encoding type.
   */
  PROTOBUF((byte) 0x01),
  /**
   * Json encoding type.
   */
  JSON((byte) 0x02),
  /**
   * Bson encoding type.
   */
  BSON((byte) 0x03),
  /**
   * Java binary encoding type.
   */
  JAVA_BINARY((byte) 0x04);

  private final byte code;

  EncodingType(byte code) {
    this.code = code;
  }

  /**
   * Gets code.
   *
   * @return the code
   */
  public byte getCode() {
    return code;
  }

  /**
   * Of encoding type.
   *
   * @param value the value
   * @return the encoding type
   */
  public static EncodingType of(byte value) {
    return Arrays.stream(EncodingType.values())
      .filter(type -> type.code == value)
      .findAny()
      .orElse(EncodingType.NULL);
  }

  @Override
  public String toString() {
    return String.format("EncodingType{%s(0x%02x)}", name(), code);
  }
}
