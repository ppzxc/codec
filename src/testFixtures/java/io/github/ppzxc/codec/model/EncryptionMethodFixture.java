package io.github.ppzxc.codec.model;

import io.github.ppzxc.codec.model.EncryptionMethod;
import io.github.ppzxc.fixh.StringUtils;

public final class EncryptionMethodFixture {

  private EncryptionMethodFixture() {
  }

  public static EncryptionMethod create(String type, String mode, String pkcs, String iv, String symmetricKey) {
    return EncryptionMethod.builder()
      .type(type)
      .mode(mode)
      .pkcs(pkcs)
      .iv(iv)
      .symmetricKey(symmetricKey)
      .build();
  }

  public static EncryptionMethod random() {
    return create(StringUtils.giveMeOne(1, 10), StringUtils.giveMeOne(1, 10), StringUtils.giveMeOne(1, 10),
      StringUtils.giveMeOne(1, 10), StringUtils.giveMeOne(1, 10));
  }
}
