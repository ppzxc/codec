package io.github.ppzxc.codec.model;

import io.github.ppzxc.codec.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public final class InboundMessageFixture {

  private InboundMessageFixture() {
  }

  public static InboundProtocol create(Header header, byte[] body) {
    return InboundProtocol.builder()
      .header(header)
      .body(body)
      .build();
  }

  public static InboundProtocol create(int length, byte[] body) {
    return create(HeaderFixture.create(length), body);
  }

  public static InboundProtocol create(byte[] body) {
    return create(HeaderFixture.create(body.length + Header.BODY_LENGTH), body);
  }

  public static ByteBuf to(InboundProtocol inboundMessage) {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeInt(inboundMessage.header().getLength());
    buffer.writeLong(inboundMessage.header().getId());
    buffer.writeByte(inboundMessage.header().getType());
    buffer.writeByte(inboundMessage.header().getStatus());
    buffer.writeByte(inboundMessage.header().getEncoding());
    buffer.writeByte(inboundMessage.header().getReserved());
    buffer.writeBytes(inboundMessage.getBody());
    buffer.writeBytes(Constants.LineDelimiter.BYTE_ARRAY);
    return buffer;
  }

  public static ByteBuf encryptedBodyOf(byte[] encryptedBody) {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeInt(encryptedBody.length + Constants.LineDelimiter.LENGTH);
    buffer.writeBytes(encryptedBody);
    buffer.writeBytes(Constants.LineDelimiter.BYTE_ARRAY);
    return buffer;
  }
}
