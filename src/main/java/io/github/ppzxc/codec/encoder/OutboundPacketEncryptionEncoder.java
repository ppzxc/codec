package io.github.ppzxc.codec.encoder;

import io.github.ppzxc.codec.exception.OutboundPacketEncryptFailedException;
import io.github.ppzxc.codec.model.EncryptedOutboundPacket;
import io.github.ppzxc.codec.model.SerializedOutboundPacket;
import io.github.ppzxc.crypto.Crypto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Outbound packet encryption encoder.
 */
public class OutboundPacketEncryptionEncoder extends MessageToMessageEncoder<SerializedOutboundPacket> {

  private final static Logger log = LoggerFactory.getLogger(OutboundPacketEncryptionEncoder.class);
  private final Crypto crypto;

  /**
   * Instantiates a new Outbound packet encryption encoder.
   *
   * @param crypto the crypto
   */
  public OutboundPacketEncryptionEncoder(Crypto crypto) {
    this.crypto = crypto;
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, SerializedOutboundPacket msg, List<Object> out) throws Exception {
    log.debug("{} encode", ctx.channel().toString());
    try {
      out.add(EncryptedOutboundPacket.builder()
        .header(msg.getHeader())
        .body(crypto.encrypt(msg.getBody()))
        .build());
    } catch (Throwable throwable) {
      throw new OutboundPacketEncryptFailedException(msg.getHeader(), throwable);
    }
  }
}
