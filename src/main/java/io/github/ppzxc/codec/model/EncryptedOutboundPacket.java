package io.github.ppzxc.codec.model;

/**
 * The type Encrypted outbound packet.
 */
public class EncryptedOutboundPacket implements OutboundPacket {

  private static final long serialVersionUID = 4145255986343487662L;
  private final Header header;
  private final byte[] body;

  private EncryptedOutboundPacket(Header header, byte[] body) {
    this.header = header;
    this.body = body;
  }

  @Override
  public Header getHeader() {
    return header;
  }

  /**
   * Get body byte [ ].
   *
   * @return the byte [ ]
   */
  public byte[] getBody() {
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
    private byte[] body;

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
    public PrepareOutboundPacketBuilder body(byte[] body) {
      this.body = body;
      return this;
    }

    /**
     * Build encrypted outbound packet.
     *
     * @return the encrypted outbound packet
     */
    public EncryptedOutboundPacket build() {
      return new EncryptedOutboundPacket(header, body);
    }
  }
}