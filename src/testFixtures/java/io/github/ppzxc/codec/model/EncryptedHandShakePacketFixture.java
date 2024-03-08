package io.github.ppzxc.codec.model;

import io.netty.buffer.ByteBuf;

public final class EncryptedHandShakePacketFixture {

  private EncryptedHandShakePacketFixture() {
  }

  public static EncryptedHandShakePacket create(Header header, ByteBuf body) {
    return EncryptedHandShakePacket.builder()
      .header(header)
      .body(body)
      .build();
  }

  public static EncryptedHandShakePacket withBody(ByteBuf body) {
    return create(HeaderFixture.handShake(body.readableBytes()), body);
  }

  public static EncryptedHandShakePacket withJsonBody(ByteBuf body) {
    return create(HeaderFixture.handShake(EncodingType.JSON, body.readableBytes()), body);
  }

  public static EncryptedHandShakePacket withBsonBody(ByteBuf body) {
    return create(HeaderFixture.handShake(EncodingType.BSON, body.readableBytes()), body);
  }
}
