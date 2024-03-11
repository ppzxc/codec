package io.github.ppzxc.codec.model;

import io.github.ppzxc.fixh.ObjectUtils;
import io.netty.buffer.ByteBuf;

/**
 * The type Inbound message.
 */
public class InboundMessage extends AbstractMessage {

  private static final long serialVersionUID = 3043297001961428382L;
  private final transient ByteBuf body;

  private InboundMessage(Header header, ByteBuf body) {
    super(header);
    this.body = body;
  }

  /**
   * Builder builder.
   *
   * @return the builder
   */
  public static Builder builder() {
    return new Builder();
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
      this.header = ObjectUtils.requireNonNull(header, "'Header' is require non null");
      return this;
    }

    /**
     * Body builder.
     *
     * @param body the body
     * @return the builder
     */
    public Builder body(ByteBuf body) {
      this.body = ObjectUtils.requireNonNull(body, "'Body' is require non null");
      return this;
    }

    /**
     * Build inbound message.
     *
     * @return the inbound message
     */
    public InboundMessage build() {
      return new InboundMessage(header, body);
    }
  }
}
