package io.github.ppzxc.codec.model;

import io.github.ppzxc.fixh.IntUtils;

public final class HandshakeHeaderFixture {

  private HandshakeHeaderFixture() {
  }

  public static HandshakeHeader create(HandshakeType handShakeType, EncryptionType encryptionType,
    EncryptionMode encryptionMode, EncryptionPadding encryptionPadding) {
    return HandshakeHeader.builder()
      .handShakeType(handShakeType)
      .encryptionType(encryptionType)
      .encryptionMode(encryptionMode)
      .encryptionPadding(encryptionPadding)
      .build();
  }

  public static HandshakeHeader random() {
    return create(HandshakeType.values()[IntUtils.giveMeOne(HandshakeType.values().length)],
      EncryptionType.values()[IntUtils.giveMeOne(EncryptionType.values().length)],
      EncryptionMode.values()[IntUtils.giveMeOne(EncryptionMode.values().length)],
      EncryptionPadding.values()[IntUtils.giveMeOne(EncryptionPadding.values().length)]);
  }
}
