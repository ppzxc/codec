package io.github.ppzxc.codec.model;

import io.github.ppzxc.fixh.ObjectUtils;
import io.netty.buffer.ByteBuf;

/**
 * The type Raw inbound packet.
 */
public class RawInboundPacket extends AbstractRawPacket implements InboundPacket {

  private static final long serialVersionUID = 3043297001961428382L;

  private RawInboundPacket(Header header, ByteBuf body) {
    super(header, body);
  }

  /**
   * Builder raw inbound packet builder.
   *
   * @return the raw inbound packet builder
   */
  public static RawInboundPacketBuilder builder() {
    return new RawInboundPacketBuilder();
  }

  @Override
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
   * The type Raw inbound packet builder.
   */
  public static final class RawInboundPacketBuilder {

    private Header header;
    private ByteBuf body;

    private RawInboundPacketBuilder() {
    }

    /**
     * Header raw inbound packet builder.
     *
     * @param header the header
     * @return the raw inbound packet builder
     */
    public RawInboundPacketBuilder header(Header header) {
      this.header = ObjectUtils.requireNonNull(header, "'Header' is require non null");
      return this;
    }

    /**
     * Body raw inbound packet builder.
     *
     * @param body the body
     * @return the raw inbound packet builder
     */
    public RawInboundPacketBuilder body(ByteBuf body) {
      this.body = ObjectUtils.requireNonNull(body, "'Body' is require non null");
      return this;
    }

    /**
     * Build raw inbound packet.
     *
     * @return the raw inbound packet
     */
    public RawInboundPacket build() {
      return new RawInboundPacket(header, body);
    }
  }
}
