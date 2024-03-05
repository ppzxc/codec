package io.github.ppzxc.codec.model;

import io.github.ppzxc.fixh.ObjectUtils;
import io.netty.buffer.ByteBuf;

public class RawOutboundPacket extends AbstractRawPacket implements OutboundPacket {

  private static final long serialVersionUID = 3043297001961428382L;

  private RawOutboundPacket(Header header, ByteBuf body) {
    super(header, body);
  }

  @Override
  public Header getHeader() {
    return header;
  }

  public ByteBuf getBody() {
    return body;
  }

  public static RawOutboundPacketBuilder builder() {
    return new RawOutboundPacketBuilder();
  }

  public static final class RawOutboundPacketBuilder {

    private Header header;
    private ByteBuf body;

    private RawOutboundPacketBuilder() {
    }

    public RawOutboundPacketBuilder header(Header header) {
      this.header = ObjectUtils.requireNonNull(header, "'Header' is require non null");
      return this;
    }

    public RawOutboundPacketBuilder body(ByteBuf body) {
      this.body = ObjectUtils.requireNonNull(body, "'Body' is require non null");
      return this;
    }

    public RawOutboundPacket build() {
      return new RawOutboundPacket(header, body);
    }
  }
}
