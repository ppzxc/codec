package io.github.ppzxc.codec.model;

/**
 * The type Decrypted hand shake packet.
 */
public class DecryptedHandShakePacket implements InboundPacket {

  private static final long serialVersionUID = -3646299368695586659L;
  private final Header header;
  private final byte[] body;

  private DecryptedHandShakePacket(Header header, byte[] body) {
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
   * Builder encrypted hand shake packet builder.
   *
   * @return the encrypted hand shake packet builder
   */
  public static EncryptedHandShakePacketBuilder builder() {
    return new EncryptedHandShakePacketBuilder();
  }

  /**
   * The type Encrypted hand shake packet builder.
   */
  public static final class EncryptedHandShakePacketBuilder {

    private Header header;
    private byte[] body;

    private EncryptedHandShakePacketBuilder() {
    }

    /**
     * Header encrypted hand shake packet builder.
     *
     * @param header the header
     * @return the encrypted hand shake packet builder
     */
    public EncryptedHandShakePacketBuilder header(Header header) {
      this.header = header;
      return this;
    }

    /**
     * Body encrypted hand shake packet builder.
     *
     * @param body the body
     * @return the encrypted hand shake packet builder
     */
    public EncryptedHandShakePacketBuilder body(byte[] body) {
      this.body = body;
      return this;
    }

    /**
     * Build decrypted hand shake packet.
     *
     * @return the decrypted hand shake packet
     */
    public DecryptedHandShakePacket build() {
      return new DecryptedHandShakePacket(header, body);
    }
  }
}
