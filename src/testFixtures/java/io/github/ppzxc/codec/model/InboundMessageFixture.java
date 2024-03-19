package io.github.ppzxc.codec.model;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public final class InboundMessageFixture {

  private InboundMessageFixture() {
  }

  public static InboundMessage create(Header header, byte[] body) {
    return InboundMessage.builder()
      .header(header)
      .body(body)
      .build();
  }

  public static InboundMessage create(int length, byte[] body) {
    return create(HeaderFixture.create(length), body);
  }

  public static InboundMessage create(byte[] body) {
    return create(HeaderFixture.create(body.length + Header.LENGTH_WITHOUT_LENGTH_FIELD), body);
  }

  public static ByteBuf to(InboundMessage inboundMessage) {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeInt(inboundMessage.header().getLength());
    buffer.writeLong(inboundMessage.header().getId());
    buffer.writeByte(inboundMessage.header().getType());
    buffer.writeByte(inboundMessage.header().getStatus());
    buffer.writeByte(inboundMessage.header().getEncoding());
    buffer.writeByte(inboundMessage.header().getReserved());
    buffer.writeBytes(inboundMessage.getBody());
    buffer.writeBytes(Header.LINE_DELIMITER);
    return buffer;
  }

  public static ByteBuf encryptedBodyOf(byte[] encryptedBody) {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeInt(encryptedBody.length + Header.LINE_DELIMITER_LENGTH);
    buffer.writeBytes(encryptedBody);
    buffer.writeBytes(Header.LINE_DELIMITER);
    return buffer;
  }
}
