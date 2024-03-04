package io.github.ppzxc.codec.model;

import io.github.ppzxc.fixh.ObjectUtils;
import io.netty.buffer.ByteBuf;

/**
 * The type Raw packet.
 */
public class RawPacket {

  private final Header header;
  private final ByteBuf body;

  /**
   * Instantiates a new Raw packet.
   *
   * @param header the header
   * @param body   the body
   */
  public RawPacket(Header header, ByteBuf body) {
    this.header = header;
    this.body = body;
  }

  /**
   * Gets header.
   *
   * @return the header
   */
  public Header getHeader() {
    return header;
  }

  /**
   * Gets body.
   *
   * @return the body
   */
  public ByteBuf getBody() {
    return body;
  }

  /**
   * Builder raw packet builder.
   *
   * @return the raw packet builder
   */
  public static RawPacketBuilder builder() {
    return new RawPacketBuilder();
  }

  /**
   * The type Raw packet builder.
   */
  public static final class RawPacketBuilder {

    private Header header;
    private ByteBuf body;

    private RawPacketBuilder() {
    }

    /**
     * A raw raw packet builder.
     *
     * @return the raw packet builder
     */
    public static RawPacketBuilder aRaw() {
      return new RawPacketBuilder();
    }

    /**
     * Header raw packet builder.
     *
     * @param header the header
     * @return the raw packet builder
     */
    public RawPacketBuilder header(Header header) {
      this.header = ObjectUtils.requireNonNull(header, "'Header' is require non null");
      return this;
    }

    /**
     * Body raw packet builder.
     *
     * @param body the body
     * @return the raw packet builder
     */
    public RawPacketBuilder body(ByteBuf body) {
      this.body = ObjectUtils.requireNonNull(body, "'Body' is require non null");
      return this;
    }

    /**
     * Build raw packet.
     *
     * @return the raw packet
     */
    public RawPacket build() {
      return new RawPacket(header, body);
    }
  }
}
