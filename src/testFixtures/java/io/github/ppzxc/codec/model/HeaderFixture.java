package io.github.ppzxc.codec.model;

import io.github.ppzxc.fixh.ByteUtils;
import io.github.ppzxc.fixh.IntUtils;
import io.github.ppzxc.fixh.LongUtils;

public final class HeaderFixture {

  private HeaderFixture() {
  }

  public static Header create(int length, long id, byte type, byte status, byte encoding, byte reserved) {
    return Header.builder()
      .length(length)
      .id(id)
      .type(type)
      .status(status)
      .encoding(encoding)
      .reserved(reserved)
      .build();
  }

  public static Header create(int length) {
    return create(length, LongUtils.giveMeOne(), ByteUtils.giveMeOne(), ByteUtils.giveMeOne(),
      ByteUtils.giveMeOne(), ByteUtils.giveMeOne());
  }

  public static Header create() {
    return create(IntUtils.giveMeNegative(), LongUtils.giveMeOne(), ByteUtils.giveMeOne(), ByteUtils.giveMeOne(),
      ByteUtils.giveMeOne(), ByteUtils.giveMeOne());
  }
}
