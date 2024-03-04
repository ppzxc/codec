package io.github.ppzxc.codec.decoder;

import io.github.ppzxc.codec.model.Header;
import io.github.ppzxc.codec.model.RawPacket;
import io.netty.buffer.ByteBuf;
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

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
    log.debug("{} decode", ctx.channel().toString());
    if (msg == null || msg.readableBytes() <= 0) {
      throw new NullPointerException("byte array require non null");
    }
    Header header = Header.builder()
      .id(msg.readInt())
      .type(msg.readByte())
      .status(msg.readByte())
      .encoding(msg.readByte())
      .reserved(msg.readByte())
      .bodyLength(msg.readInt())
      .build();
    out.add(RawPacket.builder()
      .header(header)
      .build());
  }
}
