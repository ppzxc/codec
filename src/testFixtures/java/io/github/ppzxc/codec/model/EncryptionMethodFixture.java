package io.github.ppzxc.codec.model;

import io.github.ppzxc.fixh.StringUtils;

public final class EncryptionMethodFixture {

  private EncryptionMethodFixture() {
  }

  public static EncryptionMethod create(EncryptionType type, EncryptionMode mode, EncryptionPadding padding, String iv,
    String symmetricKey) {
    return EncryptionMethod.builder()
      .type(type)
      .mode(mode)
      .padding(padding)
      .iv(iv)
      .symmetricKey(symmetricKey)
      .build();
  }

  public static EncryptionMethod random() {
    return create(giveMeEncryptionType(), giveMeEncryptionMode(), giveMeEncryptionPadding(),
      StringUtils.giveMeOne(32), StringUtils.giveMeOne(16));
  }

  public static EncryptionType giveMeEncryptionType() {
    return new RandomEnumGenerator<>(EncryptionType.class).randomEnum();
  }

  public static EncryptionMode giveMeEncryptionMode() {
    return new RandomEnumGenerator<>(EncryptionMode.class).randomEnum();
  }

  public static EncryptionPadding giveMeEncryptionPadding() {
    return new RandomEnumGenerator<>(EncryptionPadding.class).randomEnum();
  }
}
