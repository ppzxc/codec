package io.github.ppzxc.codec.model;

import io.github.ppzxc.fixh.ByteArrayUtils;
import io.github.ppzxc.fixh.IntUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public final class RawMessageFixture {

  private RawMessageFixture() {
  }

  public static InboundMessage create(Header header, ByteBuf body) {
    return InboundMessage.builder()
      .header(header)
      .body(body)
      .build();
  }

  public static InboundMessage withFakeHandShake() {
    int bodyLength = IntUtils.giveMeOne(100, 200);
    ByteBuf body = Unpooled.buffer(bodyLength + Header.MINIMUM_BODY_LENGTH);
    body.writeBytes(ByteArrayUtils.giveMeOne(bodyLength));
    body.writeBytes(AbstractMessage.LINE_DELIMITER);
    return create(HeaderFixture.handShake(bodyLength + Header.MINIMUM_BODY_LENGTH), body);
  }

  public static InboundMessage withBody(int bodyLength) {
    ByteBuf body = Unpooled.buffer(bodyLength + Header.MINIMUM_BODY_LENGTH);
    body.writeBytes(ByteArrayUtils.giveMeOne(bodyLength));
    body.writeBytes(AbstractMessage.LINE_DELIMITER);
    return create(HeaderFixture.random(bodyLength + Header.MINIMUM_BODY_LENGTH), body);
  }

  public static InboundMessage withBodyWithoutLineDelimiter(int bodyLength) {
    ByteBuf body = Unpooled.buffer(bodyLength );
    body.writeBytes(ByteArrayUtils.giveMeOne(bodyLength));
    return create(HeaderFixture.random(bodyLength ), body);
  }

  public static InboundMessage emptyBodyWithLineDelimiter() {
    return create(HeaderFixture.random(AbstractMessage.LINE_DELIMITER.length), Unpooled.copiedBuffer(
      AbstractMessage.LINE_DELIMITER));
  }

  public static ByteBuf toByteBuf(InboundMessage given) {
    ByteBuf buffer = Unpooled.buffer(Header.HEADER_LENGTH + given.header().getBodyLength());
    buffer.writeInt(given.header().getId());
    buffer.writeByte(given.header().getType());
    buffer.writeByte(given.header().getStatus());
    buffer.writeByte(given.header().getEncoding());
    buffer.writeByte(given.header().getReserved());
    buffer.writeInt(given.header().getBodyLength());
    if (given.header().getBodyLength() > 0) {
      byte[] bodyBuffer = new byte[given.getBody().readableBytes()];
      given.getBody().getBytes(0, bodyBuffer);
      buffer.writeBytes(bodyBuffer);
    }
    return buffer;
  }
}
