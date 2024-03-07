package io.github.ppzxc.codec.model;

import io.github.ppzxc.fixh.ByteUtils;
import io.github.ppzxc.fixh.IntUtils;

public final class HeaderFixture {

  private HeaderFixture() {
  }

  public static Header create(int id, byte type, byte status, byte encoding, byte reserved, int bodyLength) {
    return Header.builder()
      .id(id)
      .type(type)
      .status(status)
      .encoding(encoding)
      .reserved(reserved)
      .bodyLength(bodyLength)
      .build();
  }

  public static Header random(int bodyLength) {
    return create(IntUtils.giveMeOne(), ByteUtils.giveMeOne(), ByteUtils.giveMeOne(),
      ByteUtils.giveMeOne(), ByteUtils.giveMeOne(), bodyLength);
  }

  public static Header random() {
    return random(IntUtils.giveMePositive(1024 * 1024));
  }

  public static Header emptyBody() {
    return random(0);
  }

  public static Header handShake(int bodyLength) {
    return Header.builder()
      .id(IntUtils.giveMeOne())
      .type((byte) 0x01)
      .status((byte) 0x00)
      .encoding((byte) 0x01)
      .reserved((byte) 0x00)
      .bodyLength(bodyLength)
      .build();
  }
}