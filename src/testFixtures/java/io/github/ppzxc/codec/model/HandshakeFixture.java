package io.github.ppzxc.codec.model;

import io.github.ppzxc.fixh.ByteArrayUtils;
import io.github.ppzxc.fixh.ByteUtils;
import io.github.ppzxc.fixh.IntUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class HandshakeFixture {

  private HandshakeFixture() {
  }

  public static ByteBuf wrongHandShakeType() {
    return create(getWrongHandShakeType(), EncryptionType.ADVANCED_ENCRYPTION_STANDARD.getCode(),
      EncryptionMode.CIPHER_BLOCK_CHAINING.getCode(), EncryptionPadding.PKCS7PADDING.getCode(),
      ByteArrayUtils.giveMeOne(16), ByteArrayUtils.giveMeOne(16));
  }

  public static ByteBuf wrongEncryptionType() {
    return create(HandshakeType.RSA_1024.getCode(), getWrongEncryptionType(),
      EncryptionMode.CIPHER_BLOCK_CHAINING.getCode(),
      EncryptionPadding.PKCS7PADDING.getCode(), ByteArrayUtils.giveMeOne(16), ByteArrayUtils.giveMeOne(16));
  }

  public static ByteBuf wrongEncryptionMode() {
    return create(HandshakeType.RSA_1024.getCode(), EncryptionType.ADVANCED_ENCRYPTION_STANDARD.getCode(),
      getWrongEncryptionMode(), EncryptionPadding.PKCS7PADDING.getCode(), ByteArrayUtils.giveMeOne(16),
      ByteArrayUtils.giveMeOne(16));
  }

  public static ByteBuf wrongEncryptionPadding() {
    return create(HandshakeType.RSA_1024.getCode(), EncryptionType.ADVANCED_ENCRYPTION_STANDARD.getCode(),
      EncryptionMode.CIPHER_BLOCK_CHAINING.getCode(), getWrongEncryptionPadding(), ByteArrayUtils.giveMeOne(16),
      ByteArrayUtils.giveMeOne(16));
  }

  public static ByteBuf wrongSymmetricKeySize() {
    return create(HandshakeType.RSA_1024.getCode(), EncryptionType.ADVANCED_ENCRYPTION_STANDARD.getCode(),
      EncryptionMode.CIPHER_BLOCK_CHAINING.getCode(), EncryptionPadding.PKCS7PADDING.getCode(), ByteArrayUtils.giveMeOne(16),
      getWrongSymmetricKey());
  }

  private static byte[] getWrongSymmetricKey() {
    byte[] given = ByteArrayUtils.giveMeOne(IntUtils.giveMeOne(16, 32));
    while (given.length == 16 || given.length == 24 || given.length == 32) {
      given = ByteArrayUtils.giveMeOne(IntUtils.giveMeOne(16, 32));
    }
    return given;
  }

  private static ByteBuf create(byte code, byte code1,
    byte code2, byte wrongEncryptionPadding, byte[] bytes, byte[] bytes1) {
    ByteBuf buffer = Unpooled.buffer(HandshakeHeader.MINIMUM_LENGTH);
    buffer.writeInt(HandshakeHeader.MINIMUM_LENGTH - HandshakeHeader.LENGTH_FIELD_LENGTH);
    buffer.writeByte(code);
    buffer.writeByte(code1);
    buffer.writeByte(code2);
    buffer.writeByte(wrongEncryptionPadding);
    buffer.writeBytes(bytes);
    buffer.writeBytes(bytes1);
    return buffer;
  }

  private static byte getHandShakeType() {
    List<HandshakeType> collect = Arrays.stream(HandshakeType.values())
      .filter(type -> type != HandshakeType.NONE)
      .collect(Collectors.toList());
    return collect.get(IntUtils.giveMeOne(collect.size())).getCode();
  }

  private static byte getEncryptionType() {
    List<EncryptionType> collect = Arrays.stream(EncryptionType.values())
      .filter(type -> type != EncryptionType.NONE)
      .collect(Collectors.toList());
    return collect.get(IntUtils.giveMeOne(collect.size())).getCode();
  }

  private static byte getEncryptionMode() {
    List<EncryptionMode> collect = Arrays.stream(EncryptionMode.values())
      .filter(type -> type != EncryptionMode.NONE)
      .collect(Collectors.toList());
    return collect.get(IntUtils.giveMeOne(collect.size())).getCode();
  }

  private static byte getEncryptionPadding() {
    List<EncryptionPadding> collect = Arrays.stream(EncryptionPadding.values())
      .filter(type -> type != EncryptionPadding.NONE)
      .collect(Collectors.toList());
    return collect.get(IntUtils.giveMeOne(collect.size())).getCode();
  }

  private static byte getWrongHandShakeType() {
    List<Byte> collect = Arrays.stream(HandshakeType.values())
      .map(HandshakeType::getCode)
      .collect(Collectors.toList());
    return ByteUtils.giveMeOneWithout(collect);
  }

  private static byte getWrongEncryptionType() {
    List<Byte> collect = Arrays.stream(EncryptionType.values())
      .map(EncryptionType::getCode)
      .collect(Collectors.toList());
    return ByteUtils.giveMeOneWithout(collect);
  }

  private static byte getWrongEncryptionMode() {
    List<Byte> collect = Arrays.stream(EncryptionMode.values())
      .map(EncryptionMode::getCode)
      .collect(Collectors.toList());
    return ByteUtils.giveMeOneWithout(collect);
  }

  private static byte getWrongEncryptionPadding() {
    List<Byte> collect = Arrays.stream(EncryptionPadding.values())
      .map(EncryptionPadding::getCode)
      .collect(Collectors.toList());
    return ByteUtils.giveMeOneWithout(collect);
  }
}
