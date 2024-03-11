package io.github.ppzxc.codec.model;

import io.netty.buffer.ByteBuf;

public final class EncryptedHandShakeMessageFixture {

  private EncryptedHandShakeMessageFixture() {
  }

  public static EncryptedHandShakeMessage create(Header header, ByteBuf body) {
    return EncryptedHandShakeMessage.builder()
      .header(header)
      .body(body)
      .build();
  }

  public static EncryptedHandShakeMessage withBody(ByteBuf body) {
    return create(HeaderFixture.handShake(body.readableBytes()), body);
  }

  public static EncryptedHandShakeMessage withJsonBody(ByteBuf body) {
    return create(HeaderFixture.handShake(EncodingType.JSON, body.readableBytes()), body);
  }

  public static EncryptedHandShakeMessage withBsonBody(ByteBuf body) {
    return create(HeaderFixture.handShake(EncodingType.BSON, body.readableBytes()), body);
  }
}
