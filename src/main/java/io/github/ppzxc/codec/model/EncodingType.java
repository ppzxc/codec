package io.github.ppzxc.codec.model;

import java.util.Arrays;

public enum EncodingType {
  NULL((byte) 0x00),
  PROTOBUF((byte) 0x01),
  JSON((byte) 0x02),
  BSON((byte) 0x03),
  JAVA_SERIALIZE((byte) 0x04);

  private final byte code;

  EncodingType(byte code) {
    this.code = code;
  }

  public byte getCode() {
    return code;
  }

  public static EncodingType of(byte value) {
    return Arrays.stream(EncodingType.values())
      .filter(type -> type.code == value)
      .findAny()
      .orElse(EncodingType.NULL);
  }

  @Override
  public String toString() {
    return String.format("EncodingType.%s(0x%02x)", name(), code);
  }
}
