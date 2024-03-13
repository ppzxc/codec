package io.github.ppzxc.codec.model;

import io.netty.buffer.ByteBuf;

public class EncryptedHandShakeMessage extends AbstractMessage {

  private static final long serialVersionUID = -3612459006902545202L;
  private final transient ByteBuf body;

  private EncryptedHandShakeMessage(Header header, ByteBuf body) {
    super(header);
    this.body = body;
  }

  @Override
  public Header header() {
    return header;
  }

  public ByteBuf getBody() {
    return body;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {

    private Header header;
    private ByteBuf body;

    private Builder() {
    }

    public Builder header(Header header) {
      this.header = header;
      return this;
    }

    public Builder body(ByteBuf body) {
      this.body = body;
      return this;
    }

    public EncryptedHandShakeMessage build() {
      return new EncryptedHandShakeMessage(header, body);
    }
  }
}
