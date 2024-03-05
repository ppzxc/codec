package io.github.ppzxc.codec.encoder;

import io.github.ppzxc.codec.exception.RawPacketToByteStreamFailedException;
import io.github.ppzxc.codec.model.Header;
import io.github.ppzxc.codec.model.RawOutboundPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Raw outbound packet encoder.
 */
public class RawOutboundPacketEncoder extends MessageToMessageEncoder<RawOutboundPacket> {

  private final static Logger log = LoggerFactory.getLogger(RawOutboundPacketEncoder.class);

  @Override
  protected void encode(ChannelHandlerContext ctx, RawOutboundPacket msg, List<Object> out) throws Exception {
    log.debug("{} encode", ctx.channel().toString());
    try {
      ByteBuf buffer = Unpooled.buffer(Header.HEADER_LENGTH + msg.getBody().readableBytes());
      buffer.writeInt(msg.getHeader().getId());
      buffer.writeByte(msg.getHeader().getType());
      buffer.writeByte(msg.getHeader().getStatus());
      buffer.writeByte(msg.getHeader().getEncoding());
      buffer.writeByte(msg.getHeader().getReserved());
      buffer.writeInt(msg.getHeader().getBodyLength());
      buffer.writeBytes(msg.getBody());
      out.add(buffer);
    } catch (Throwable throwable) {
      throw new RawPacketToByteStreamFailedException(msg.getHeader(), throwable);
    }
  }
}
