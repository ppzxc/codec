package io.github.ppzxc.codec.model;

import io.github.ppzxc.fixh.ByteArrayUtils;
import io.github.ppzxc.fixh.ByteUtils;
import io.github.ppzxc.fixh.RandomUtils;
import java.nio.ByteBuffer;

public final class ByteArrayFixture {

  public static final RandomUtils RANDOM_UTILS = RandomUtils.getInstance();

  private ByteArrayFixture() {
  }

  public static byte[] create(int id, byte type, byte status, byte encoding, byte reserved, int bodyLength,
    byte[] body) {
    ByteBuffer buffer = ByteBuffer.allocate(Header.HEADER_LENGTH + bodyLength);
    buffer.putInt(id);
    buffer.put(type);
    buffer.put(status);
    buffer.put(encoding);
    buffer.put(reserved);
    buffer.putInt(bodyLength);
    if (body != null && body.length > 0) {
      buffer.put(body);
    }
    return buffer.array();
  }

  public static byte[] random(byte[] body) {
    return create(RANDOM_UTILS.integer(), ByteUtils.giveMeOne(), ByteUtils.giveMeOne(), ByteUtils.giveMeOne(),
      ByteUtils.giveMeOne(), body.length, body);
  }

  public static byte[] randomWithEmptyBody() {
    return random(new byte[0]);
  }

  public static byte[] randomWithBody(int bodyLength) {
    ByteBuffer buffer = ByteBuffer.allocate(Header.HEADER_LENGTH);
    buffer.putInt(RANDOM_UTILS.integer());
    buffer.put(ByteUtils.giveMeOne());
    buffer.put(ByteUtils.giveMeOne());
    buffer.put(ByteUtils.giveMeOne());
    buffer.put(ByteUtils.giveMeOne());
    buffer.putInt(bodyLength);
    if (bodyLength > 0) {
      buffer.put(ByteArrayUtils.giveMeOne(bodyLength));
    }
    return buffer.array();
  }

  public static byte[] of(RawInboundPacket rawInboundPacket) {
    return create(
      rawInboundPacket.getHeader().getId(), rawInboundPacket.getHeader().getType(), rawInboundPacket.getHeader().getStatus(), rawInboundPacket.getHeader().getEncoding(), rawInboundPacket.getHeader().getReserved(), rawInboundPacket.getHeader().getBodyLength(), rawInboundPacket.getBody().array());
  }
}
