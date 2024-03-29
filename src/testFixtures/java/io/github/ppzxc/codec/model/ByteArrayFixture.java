package io.github.ppzxc.codec.model;

import io.github.ppzxc.codec.Constants;
import io.github.ppzxc.fixh.ByteArrayUtils;
import io.github.ppzxc.fixh.IntUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public final class ByteArrayFixture {

  private ByteArrayFixture() {
  }

  public static byte[] create(int bodyLength, byte[] body) {
    ByteBuf buffer = Unpooled.buffer(body.length + Header.LENGTH_FIELD_LENGTH);
    buffer.writeInt(bodyLength);
    buffer.writeBytes(body);
    return buffer.array();
  }

  public static byte[] createWithLineDelimiter(int bodyLength, byte[] body) {
    ByteBuf buffer = Unpooled.buffer(body.length + Header.MINIMUM_LENGTH);
    buffer.writeInt(bodyLength);
    buffer.writeBytes(body);
    buffer.writeBytes(Constants.LineDelimiter.BYTE_ARRAY);
    return buffer.array();
  }

  public static byte[] create(int bodyLength) {
    ByteBuf buffer = Unpooled.buffer(
      Header.LENGTH_FIELD_LENGTH + Math.max(0, bodyLength));
    buffer.writeInt(bodyLength);
    if (bodyLength > 0) {
      buffer.writeBytes(ByteArrayUtils.giveMeOne(bodyLength));
    }
    return buffer.array();
  }

  public static byte[] create(byte[] body) {
    ByteBuf buffer = Unpooled.buffer(
      Header.LENGTH_FIELD_LENGTH + body.length);
    buffer.writeInt(body.length);
    if (body.length > 0) {
      buffer.writeBytes(body);
    }
    return buffer.array();
  }

  public static byte[] random(byte[] body) {
    return create(body);
  }

  public static byte[] random() {
    return random(ByteArrayUtils.giveMeOne(IntUtils.giveMeOne(64, 128)));
  }

  public static ByteBuf create(InboundProtocol given) {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeInt(given.header().getLength());
    buffer.writeLong(given.header().getId());
    buffer.writeByte(given.header().getType());
    buffer.writeByte(given.header().getStatus());
    buffer.writeByte(given.header().getEncoding());
    buffer.writeByte(given.header().getReserved());
    buffer.writeBytes(given.getBody());
    buffer.writeBytes(Constants.LineDelimiter.BYTE_ARRAY);
    return buffer;
  }

  public static ByteBuf withoutLengthFieldAndLineDelimiter(InboundProtocol given) {
    ByteBuf buffer = Unpooled.buffer(Header.ID_FIELD_LENGTH + Header.PROTOCOL_FIELDS_LENGTH + given.getBody().length);
    buffer.writeLong(given.header().getId());
    buffer.writeByte(given.header().getType());
    buffer.writeByte(given.header().getStatus());
    buffer.writeByte(given.header().getEncoding());
    buffer.writeByte(given.header().getReserved());
    buffer.writeBytes(given.getBody());
    return buffer;
  }

  public static ByteBuf withoutLineDelimiter(InboundProtocol given) {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeInt(Header.LENGTH_FIELD_LENGTH + given.getBody().length);
    buffer.writeLong(given.header().getId());
    buffer.writeByte(given.header().getType());
    buffer.writeByte(given.header().getStatus());
    buffer.writeByte(given.header().getEncoding());
    buffer.writeByte(given.header().getReserved());
    buffer.writeBytes(given.getBody());
    return buffer;
  }

  public static ByteBuf withoutLineDelimiter() {
    int length = IntUtils.giveMeOne(256, 512);
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeInt(length);
    buffer.writeBytes(ByteArrayUtils.giveMeOne(length));
    return buffer;
  }

  public static ByteBuf withLineDelimiter(int length, byte[] body) {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeInt(length);
    buffer.writeBytes(body);
    buffer.writeBytes(Constants.LineDelimiter.BYTE_ARRAY);
    return buffer;
  }
}
