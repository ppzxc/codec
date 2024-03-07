package io.github.ppzxc.codec.model;

import io.github.ppzxc.fixh.ObjectUtils;
import io.netty.buffer.ByteBuf;

/**
 * The type Raw outbound packet.
 */
public class RawOutboundPacket extends AbstractRawPacket implements OutboundPacket {

  private static final long serialVersionUID = 3043297001961428382L;

  private RawOutboundPacket(Header header, ByteBuf body) {
    super(header, body);
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
   * Builder raw outbound packet builder.
   *
   * @return the raw outbound packet builder
   */
  public static RawOutboundPacketBuilder builder() {
    return new RawOutboundPacketBuilder();
  }

  /**
   * The type Raw outbound packet builder.
   */
  public static final class RawOutboundPacketBuilder {

    private Header header;
    private ByteBuf body;

    private RawOutboundPacketBuilder() {
    }

    /**
     * Header raw outbound packet builder.
     *
     * @param header the header
     * @return the raw outbound packet builder
     */
    public RawOutboundPacketBuilder header(Header header) {
      this.header = ObjectUtils.requireNonNull(header, "'Header' is require non null");
      return this;
    }

    /**
     * Body raw outbound packet builder.
     *
     * @param body the body
     * @return the raw outbound packet builder
     */
    public RawOutboundPacketBuilder body(ByteBuf body) {
      this.body = ObjectUtils.requireNonNull(body, "'Body' is require non null");
      return this;
    }

    /**
     * Build raw outbound packet.
     *
     * @return the raw outbound packet
     */
    public RawOutboundPacket build() {
      return new RawOutboundPacket(header, body);
    }
  }
}
