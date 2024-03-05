package io.github.ppzxc.codec.decoder;

import io.github.ppzxc.codec.exception.HandShakeDecryptFailedException;
import io.github.ppzxc.codec.model.DecryptedHandShakePacket;
import io.github.ppzxc.codec.model.EncryptedHandShakePacket;
import io.github.ppzxc.crypto.Crypto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Hand shake decrypt decoder.
 */
public class HandShakeDecryptDecoder extends MessageToMessageDecoder<EncryptedHandShakePacket> {

  private final static Logger log = LoggerFactory.getLogger(HandShakeDecryptDecoder.class);
  private final Crypto crypto;

  /**
   * Instantiates a new Hand shake decrypt decoder.
   *
   * @param crypto the crypto
   */
  public HandShakeDecryptDecoder(Crypto crypto) {
    this.crypto = crypto;
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, EncryptedHandShakePacket msg, List<Object> out) throws Exception {
    log.debug("{} decode", ctx.channel().toString());
    try {
      out.add(DecryptedHandShakePacket.builder()
        .header(msg.getHeader())
        .body(crypto.decrypt(msg.getBody().array()))
        .build());
    } catch (Throwable e) {
      throw new HandShakeDecryptFailedException(msg.getHeader(), e);
    }
  }
}
