package io.github.ppzxc.codec.model;

import io.github.ppzxc.fixh.ByteArrayUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public final class RawInboundPacketFixture {

  private RawInboundPacketFixture() {
  }

  public static RawInboundPacket create(Header header, ByteBuf body) {
    return RawInboundPacket.builder()
      .header(header)
      .body(body)
      .build();
  }

  public static RawInboundPacket withBody(int bodyLength) {
    ByteBuf body = Unpooled.buffer(bodyLength + Header.MINIMUM_BODY_LENGTH);
    body.writeBytes(ByteArrayUtils.giveMeOne(bodyLength));
    body.writeBytes(RawInboundPacket.LINE_DELIMITER);
    return create(HeaderFixture.random(bodyLength + Header.MINIMUM_BODY_LENGTH), body);
  }

  public static RawInboundPacket emptyBody() {
    return create(HeaderFixture.emptyBody(), Unpooled.buffer(0));
  }

  public static RawInboundPacket emptyBodyWithLineDelimiter() {
    return create(HeaderFixture.random(RawInboundPacket.LINE_DELIMITER.length), Unpooled.copiedBuffer(RawInboundPacket.LINE_DELIMITER));
  }

  public static ByteBuf toByteBuf(RawInboundPacket given) {
    ByteBuf buffer = Unpooled.buffer(Header.HEADER_LENGTH + given.getHeader().getBodyLength());
    buffer.writeInt(given.getHeader().getId());
    buffer.writeByte(given.getHeader().getType());
    buffer.writeByte(given.getHeader().getStatus());
    buffer.writeByte(given.getHeader().getEncoding());
    buffer.writeByte(given.getHeader().getReserved());
    buffer.writeInt(given.getHeader().getBodyLength());
    if (given.getHeader().getBodyLength() > 0) {
      byte[] bodyBuffer = new byte[given.getBody().readableBytes()];
      given.getBody().getBytes(0, bodyBuffer);
      buffer.writeBytes(bodyBuffer);
    }
    return buffer;
  }
}
