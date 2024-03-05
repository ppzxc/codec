package io.github.ppzxc.codec.model;

import io.netty.buffer.ByteBuf;

public abstract class AbstractRawPacket {

  public static final int MINIMUM_PACKET_LENGTH = Header.HEADER_LENGTH + Header.MINIMUM_BODY_LENGTH;
  public static final byte[] LINE_DELIMITER = new byte[]{'\r', '\n'};
  protected final Header header;
  protected final ByteBuf body;

  protected AbstractRawPacket(Header header, ByteBuf body) {
    this.header = header;
    this.body = body;
  }
}
