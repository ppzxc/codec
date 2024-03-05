package io.github.ppzxc.codec.encoder;

import io.github.ppzxc.codec.exception.OutboundPacketSerializeFailedException;
import io.github.ppzxc.codec.model.PrepareOutboundPacket;
import io.github.ppzxc.codec.model.SerializedOutboundPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Abstract prepare outbound packet serialize encoder.
 */
public abstract class AbstractPrepareOutboundPacketSerializeEncoder extends
  MessageToMessageEncoder<PrepareOutboundPacket> {

  private final static Logger log = LoggerFactory.getLogger(AbstractPrepareOutboundPacketSerializeEncoder.class);

  /**
   * Write byte [ ].
   *
   * @param object the object
   * @return the byte [ ]
   */
  public abstract byte[] write(Object object);

  @Override
  protected void encode(ChannelHandlerContext ctx, PrepareOutboundPacket prepareOutboundPacket, List<Object> out)
    throws Exception {
    log.debug("{} encode", ctx.channel().toString());
    try {
      out.add(SerializedOutboundPacket.builder()
        .header(prepareOutboundPacket.getHeader())
        .body(write(prepareOutboundPacket.getBody()))
        .build());
    } catch (Throwable throwable) {
      throw new OutboundPacketSerializeFailedException(prepareOutboundPacket.getHeader(), throwable);
    }
  }
}
