package io.github.ppzxc.codec.model;

import io.github.ppzxc.fixh.ObjectUtils;
import io.netty.buffer.ByteBuf;

public class RawInboundPacket extends AbstractRawPacket implements InboundPacket {

  private static final long serialVersionUID = 3043297001961428382L;

  private RawInboundPacket(Header header, ByteBuf body) {
    super(header, body);
  }

  public static RawInboundPacketBuilder builder() {
    return new RawInboundPacketBuilder();
  }

  @Override
  public Header getHeader() {
    return header;
  }

  public ByteBuf getBody() {
    return body;
  }

  public static final class RawInboundPacketBuilder {

    private Header header;
    private ByteBuf body;

    private RawInboundPacketBuilder() {
    }

    public RawInboundPacketBuilder header(Header header) {
      this.header = ObjectUtils.requireNonNull(header, "'Header' is require non null");
      return this;
    }

    public RawInboundPacketBuilder body(ByteBuf body) {
      this.body = ObjectUtils.requireNonNull(body, "'Body' is require non null");
      return this;
    }

    public RawInboundPacket build() {
      return new RawInboundPacket(header, body);
    }
  }
}
