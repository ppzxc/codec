package io.github.ppzxc.codec.decoder;

import io.github.ppzxc.codec.exception.HandShakeDecodeFailException;
import io.github.ppzxc.codec.model.EncryptedHandShakePacket;
import io.github.ppzxc.codec.model.EncryptionMethod;
import io.github.ppzxc.codec.model.HandShakePacket;
import io.github.ppzxc.codec.service.Mapper;
import io.github.ppzxc.crypto.Crypto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Hand shake decoder.
 */
public class HandShakeDecoder extends MessageToMessageDecoder<EncryptedHandShakePacket> {

  private final static Logger log = LoggerFactory.getLogger(HandShakeDecoder.class);
  private final Crypto crypto;
  private final Mapper mapper;

  /**
   * Instantiates a new Hand shake decoder.
   *
   * @param crypto the crypto
   * @param mapper the mapper
   */
  public HandShakeDecoder(Crypto crypto, Mapper mapper) {
    this.crypto = crypto;
    this.mapper = mapper;
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, EncryptedHandShakePacket msg, List<Object> out) throws Exception {
    log.debug("{} decode", ctx.channel().toString());
    try {
      byte[] plainText = crypto.decrypt(msg.getBody().array());
      EncryptionMethod encryptionMethod = mapper.read(msg.getHeader().getEncoding(), plainText, EncryptionMethod.class);
      out.add(HandShakePacket.builder()
        .header(msg.getHeader())
        .encryptionMethod(encryptionMethod)
        .build());
    } catch (Throwable e) {
      throw new HandShakeDecodeFailException(msg.getHeader(), e);
    }
  }
}
