package io.github.ppzxc.codec.model;

import io.netty.buffer.ByteBuf;

/**
 * The type Abstract raw packet.
 */
public abstract class AbstractRawPacket {

  /**
   * The constant MINIMUM_PACKET_LENGTH.
   */
  public static final int MINIMUM_PACKET_LENGTH = Header.HEADER_LENGTH + Header.MINIMUM_BODY_LENGTH;
  /**
   * The constant LINE_DELIMITER.
   */
  public static final byte[] LINE_DELIMITER = new byte[]{'\r', '\n'};
  /**
   * The Header.
   */
  protected final Header header;
  /**
   * The Body.
   */
  protected final ByteBuf body;

  /**
   * Instantiates a new Abstract raw packet.
   *
   * @param header the header
   * @param body   the body
   */
  protected AbstractRawPacket(Header header, ByteBuf body) {
    this.header = header;
    this.body = body;
  }
}
