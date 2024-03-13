package io.github.ppzxc.codec.model;

import io.github.ppzxc.fixh.ObjectUtils;
import io.netty.buffer.ByteBuf;

public class InboundMessage extends AbstractMessage {

  private static final long serialVersionUID = 3043297001961428382L;
  private final transient ByteBuf body;

  private InboundMessage(Header header, ByteBuf body) {
    super(header);
    this.body = body;
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public Header header() {
    return header;
  }

  public ByteBuf getBody() {
    return body;
  }

  public static final class Builder {

    private Header header;
    private ByteBuf body;

    private Builder() {
    }

    public Builder header(Header header) {
      this.header = ObjectUtils.requireNonNull(header, "'Header' is require non null");
      return this;
    }

    public Builder body(ByteBuf body) {
      this.body = ObjectUtils.requireNonNull(body, "'Body' is require non null");
      return this;
    }

    public InboundMessage build() {
      return new InboundMessage(header, body);
    }
  }
}
