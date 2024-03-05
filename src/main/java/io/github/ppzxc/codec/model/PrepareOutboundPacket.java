package io.github.ppzxc.codec.model;

/**
 * The type Prepare outbound packet.
 */
public class PrepareOutboundPacket implements OutboundPacket {

  private static final long serialVersionUID = 8111315643882184475L;
  private final Header header;
  private final Object body;

  private PrepareOutboundPacket(Header header, Object body) {
    this.header = header;
    this.body = body;
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
  public Object getBody() {
    return body;
  }

  /**
   * Builder prepare outbound packet builder.
   *
   * @return the prepare outbound packet builder
   */
  public static PrepareOutboundPacketBuilder builder() {
    return new PrepareOutboundPacketBuilder();
  }

  /**
   * The type Prepare outbound packet builder.
   */
  public static final class PrepareOutboundPacketBuilder {

    private Header header;
    private Object body;

    private PrepareOutboundPacketBuilder() {
    }

    /**
     * Header prepare outbound packet builder.
     *
     * @param header the header
     * @return the prepare outbound packet builder
     */
    public PrepareOutboundPacketBuilder header(Header header) {
      this.header = header;
      return this;
    }

    /**
     * Body prepare outbound packet builder.
     *
     * @param body the body
     * @return the prepare outbound packet builder
     */
    public PrepareOutboundPacketBuilder body(Object body) {
      this.body = body;
      return this;
    }

    /**
     * Build prepare outbound packet.
     *
     * @return the prepare outbound packet
     */
    public PrepareOutboundPacket build() {
      return new PrepareOutboundPacket(header, body);
    }
  }
}
