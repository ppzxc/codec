package io.github.ppzxc.codec.model;

import io.github.ppzxc.fixh.ObjectUtils;
import io.netty.buffer.ByteBuf;

public class DividedRawPacket {

  private final ByteBuf header;
  private final ByteBuf body;

  public DividedRawPacket(ByteBuf header, ByteBuf body) {
    this.header = header;
    this.body = body;
  }

  public ByteBuf getHeader() {
    return header;
  }

  public ByteBuf getBody() {
    return body;
  }

  public static DividedRawPacketBuilder builder() {
    return new DividedRawPacketBuilder();
  }

  public static final class DividedRawPacketBuilder {

    private ByteBuf header;
    private ByteBuf body;

    private DividedRawPacketBuilder() {
    }

    public static DividedRawPacketBuilder aDividedRawPacket() {
      return new DividedRawPacketBuilder();
    }

    public DividedRawPacketBuilder header(ByteBuf header) {
      this.header = ObjectUtils.requireNonNull(header, new NullPointerException("'Header' is require non null"));
      return this;
    }

    public DividedRawPacketBuilder body(ByteBuf body) {
      this.body = ObjectUtils.requireNonNull(body, new NullPointerException("'Body' is require non null"));
      return this;
    }

    public DividedRawPacket build() {
      return new DividedRawPacket(header, body);
    }
  }
}
