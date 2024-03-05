package io.github.ppzxc.codec.decoder;

import io.github.ppzxc.codec.exception.HandShakeDeserializeFailedException;
import io.github.ppzxc.codec.model.DecryptedHandShakePacket;
import io.github.ppzxc.codec.model.EncryptionMethod;
import io.github.ppzxc.codec.model.HandShakePacket;
import io.github.ppzxc.codec.service.Mapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Hand shake deserialize decoder.
 */
public class HandShakeDeserializeDecoder extends MessageToMessageDecoder<DecryptedHandShakePacket> {

  private final static Logger log = LoggerFactory.getLogger(HandShakeDeserializeDecoder.class);
  private final Mapper mapper;

  /**
   * Instantiates a new Hand shake deserialize decoder.
   *
   * @param mapper the mapper
   */
  public HandShakeDeserializeDecoder(Mapper mapper) {
    this.mapper = mapper;
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, DecryptedHandShakePacket msg, List<Object> out) throws Exception {
    log.debug("{} decode", ctx.channel().toString());
    try {
      out.add(HandShakePacket.builder()
        .header(msg.getHeader())
        .encryptionMethod(mapper.read(msg.getBody(), EncryptionMethod.class))
        .build());
    } catch (Throwable e) {
      throw new HandShakeDeserializeFailedException(msg.getHeader(), e);
    }
  }
}
