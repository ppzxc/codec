package io.github.ppzxc.codec.model;

import io.netty.buffer.ByteBuf;

/**
 * The type Encrypted hand shake packet.
 */
public class EncryptedHandShakePacket implements InboundPacket {

  private static final long serialVersionUID = -3612459006902545202L;
  private final Header header;
  private final ByteBuf body;

  private EncryptedHandShakePacket(Header header, ByteBuf body) {
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
  public ByteBuf getBody() {
    return body;
  }

  /**
   * Builder builder.
   *
   * @return the builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * The type Builder.
   */
  public static final class Builder {

    private Header header;
    private ByteBuf body;

    private Builder() {
    }

    /**
     * Header builder.
     *
     * @param header the header
     * @return the builder
     */
    public Builder header(Header header) {
      this.header = header;
      return this;
    }

    /**
     * Body builder.
     *
     * @param body the body
     * @return the builder
     */
    public Builder body(ByteBuf body) {
      this.body = body;
      return this;
    }

    /**
     * Build encrypted hand shake packet.
     *
     * @return the encrypted hand shake packet
     */
    public EncryptedHandShakePacket build() {
      return new EncryptedHandShakePacket(header, body);
    }
  }
}
