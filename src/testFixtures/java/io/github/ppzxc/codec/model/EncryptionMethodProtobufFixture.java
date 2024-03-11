package io.github.ppzxc.codec.model;

import io.github.ppzxc.codec.model.protobuf.EncryptionMethodProtobuf;
import io.github.ppzxc.codec.model.protobuf.EncryptionModeProtobuf;
import io.github.ppzxc.codec.model.protobuf.EncryptionPaddingProtobuf;
import io.github.ppzxc.codec.model.protobuf.EncryptionTypeProtobuf;
import io.github.ppzxc.fixh.IntUtils;
import io.github.ppzxc.fixh.RandomUtils;

public final class EncryptionMethodProtobufFixture {

  private EncryptionMethodProtobufFixture() {
  }

  public static EncryptionMethodProtobuf random() {
    return EncryptionMethodProtobuf.newBuilder()
      .setType(getEncryptionTypeProtobuf())
      .setMode(getEncryptionModeProtobuf())
      .setPadding(getEncryptionPaddingProtobuf())
      .setIv(RandomUtils.getInstance().string(16))
      .setSymmetricKey(RandomUtils.getInstance().string(32))
      .build();
  }

  public static EncryptionTypeProtobuf getEncryptionTypeProtobuf() {
    EncryptionTypeProtobuf type = EncryptionTypeProtobuf.values()[IntUtils.giveMeOne(EncryptionTypeProtobuf.values().length)];
    while (type == EncryptionTypeProtobuf.UNRECOGNIZED) {
      type = EncryptionTypeProtobuf.values()[IntUtils.giveMeOne(EncryptionTypeProtobuf.values().length)];
    }
    return type;
  }

  public static EncryptionModeProtobuf getEncryptionModeProtobuf() {
    EncryptionModeProtobuf type = EncryptionModeProtobuf.values()[IntUtils.giveMeOne(EncryptionModeProtobuf.values().length)];
    while (type == EncryptionModeProtobuf.UNRECOGNIZED) {
      type = EncryptionModeProtobuf.values()[IntUtils.giveMeOne(EncryptionModeProtobuf.values().length)];
    }
    return type;
  }

  public static EncryptionPaddingProtobuf getEncryptionPaddingProtobuf() {
    EncryptionPaddingProtobuf type = EncryptionPaddingProtobuf.values()[IntUtils.giveMeOne(EncryptionPaddingProtobuf.values().length)];
    while (type == EncryptionPaddingProtobuf.UNRECOGNIZED) {
      type = EncryptionPaddingProtobuf.values()[IntUtils.giveMeOne(EncryptionPaddingProtobuf.values().length)];
    }
    return type;
  }
}
