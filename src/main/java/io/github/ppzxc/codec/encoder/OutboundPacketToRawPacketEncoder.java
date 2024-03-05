package io.github.ppzxc.codec.encoder;

import io.github.ppzxc.codec.exception.OutboundPacketToRawFailedException;
import io.github.ppzxc.codec.model.AbstractRawPacket;
import io.github.ppzxc.codec.model.EncryptedOutboundPacket;
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
 * The type Outbound packet to raw packet encoder.
 */
public class OutboundPacketToRawPacketEncoder extends MessageToMessageEncoder<EncryptedOutboundPacket> {

  private final static Logger log = LoggerFactory.getLogger(OutboundPacketToRawPacketEncoder.class);

  @Override
  protected void encode(ChannelHandlerContext ctx, EncryptedOutboundPacket msg, List<Object> out) throws Exception {
    log.debug("{} encode", ctx.channel().toString());
    try {
      int bodyLength = msg.getBody().length + Header.MINIMUM_BODY_LENGTH;
      Header header = Header.builder()
        .id(msg.getHeader().getId())
        .type(msg.getHeader().getType())
        .status(msg.getHeader().getStatus())
        .encoding(msg.getHeader().getEncoding())
        .reserved(msg.getHeader().getReserved())
        .bodyLength(bodyLength)
        .build();
      ByteBuf body = Unpooled.buffer(bodyLength);
      body.writeBytes(msg.getBody());
      body.writeBytes(AbstractRawPacket.LINE_DELIMITER);
      out.add(RawOutboundPacket.builder()
        .header(header)
        .body(body)
        .build());
    } catch (Throwable throwable) {
      throw new OutboundPacketToRawFailedException(msg.getHeader(), throwable);
    }
  }
}
