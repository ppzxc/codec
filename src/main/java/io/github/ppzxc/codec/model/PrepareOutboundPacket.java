package io.github.ppzxc.codec.model;

/**
 * The type Prepare outbound packet.
 */
public class PrepareOutboundPacket implements OutboundPacket {

  private static final long serialVersionUID = 8111315643882184475L;
  private Header header;
  private Object body;

  /**
   * Instantiates a new Prepare outbound packet.
   */
  public PrepareOutboundPacket() {
  }

  /**
   * Instantiates a new Prepare outbound packet.
   *
   * @param header the header
   * @param body   the body
   */
  public PrepareOutboundPacket(Header header, Object body) {
    this.header = header;
    this.body = body;
  }

  @Override
  public Header getHeader() {
    return header;
  }

  /**
   * Sets header.
   *
   * @param header the header
   */
  public void setHeader(Header header) {
    this.header = header;
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
   * Sets body.
   *
   * @param body the body
   */
  public void setBody(Object body) {
    this.body = body;
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
