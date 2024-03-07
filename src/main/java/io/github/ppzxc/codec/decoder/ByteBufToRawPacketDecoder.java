package io.github.ppzxc.codec.decoder;

import io.github.ppzxc.codec.exception.LessThanMinimumPacketLengthCodeException;
import io.github.ppzxc.codec.exception.MissingLineDelimiterCodeException;
import io.github.ppzxc.codec.exception.NotSameLengthCodeException;
import io.github.ppzxc.codec.exception.NotSupportedBodyLengthException;
import io.github.ppzxc.codec.exception.NullPointerCodeException;
import io.github.ppzxc.codec.model.AbstractRawPacket;
import io.github.ppzxc.codec.model.Header;
import io.github.ppzxc.codec.model.RawInboundPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Byte buf to raw packet decoder.
 */
public class ByteBufToRawPacketDecoder extends MessageToMessageDecoder<ByteBuf> {

  private final static Logger log = LoggerFactory.getLogger(ByteBufToRawPacketDecoder.class);
  private final int maximumBodyLength;

  /**
   * Instantiates a new Byte buf to raw packet decoder.
   *
   * @param maximumBodyLength the maximum body length
   */
  public ByteBufToRawPacketDecoder(int maximumBodyLength) {
    this.maximumBodyLength = maximumBodyLength;
  }

  /**
   * Instantiates a new Byte buf to raw packet decoder.
   */
  public ByteBufToRawPacketDecoder() {
    this(1024 * 1024 * 4); // 4 mb, 4 mega bytes
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
    log.debug("{} decode", ctx.channel().toString());
    preCondition(msg);
    Header header = Header.builder()
      .id(msg.readInt())
      .type(msg.readByte())
      .status(msg.readByte())
      .encoding(msg.readByte())
      .reserved(msg.readByte())
      .bodyLength(msg.readInt())
      .build();
    RawInboundPacket rawInboundPacket = RawInboundPacket.builder()
      .header(header)
      .body(getBody(header, msg))
      .build();
    postCondition(rawInboundPacket);
    out.add(rawInboundPacket);
  }

  private void preCondition(ByteBuf msg) throws Exception {
    if (msg.readableBytes() <= 0) {
      throw new NullPointerCodeException("byte array require non null");
    }
    if (msg.readableBytes() < AbstractRawPacket.MINIMUM_PACKET_LENGTH) {
      throw new LessThanMinimumPacketLengthCodeException(
        msg.readableBytes() + " less than " + RawInboundPacket.MINIMUM_PACKET_LENGTH);
    }
  }

  private ByteBuf getBody(Header header, ByteBuf msg) throws Exception {
    if (header.getBodyLength() > maximumBodyLength || msg.readableBytes() > maximumBodyLength) {
      throw new NotSupportedBodyLengthException(header);
    }
    if (header.getBodyLength() != msg.readableBytes()) {
      throw new NotSameLengthCodeException(header, msg.readableBytes());
    }
    return msg.readBytes(msg.readableBytes());
  }

  private void postCondition(RawInboundPacket rawInboundPacket) throws Exception {
    if (isNotContainsLineDelimiter(rawInboundPacket.getBody())) {
      throw new MissingLineDelimiterCodeException(rawInboundPacket.getHeader());
    }
  }

  private boolean isNotContainsLineDelimiter(ByteBuf body) {
    return !ByteBufUtil.equals(body, body.readableBytes() - Header.MINIMUM_BODY_LENGTH,
      Unpooled.wrappedBuffer(new byte[]{'\r', '\n'}), 0, Header.MINIMUM_BODY_LENGTH);
  }
}
