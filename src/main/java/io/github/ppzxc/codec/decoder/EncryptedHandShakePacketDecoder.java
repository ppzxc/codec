package io.github.ppzxc.codec.decoder;

import io.github.ppzxc.codec.exception.HandShakeDecodeFailException;
import io.github.ppzxc.codec.mapper.MultiMapper;
import io.github.ppzxc.codec.mapper.ReadCommand;
import io.github.ppzxc.codec.model.EncodingType;
import io.github.ppzxc.codec.model.EncryptedHandShakePacket;
import io.github.ppzxc.codec.model.EncryptionMethod;
import io.github.ppzxc.codec.model.HandShakePacket;
import io.github.ppzxc.crypto.Crypto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Encrypted hand shake packet decoder.
 */
public class EncryptedHandShakePacketDecoder extends MessageToMessageDecoder<EncryptedHandShakePacket> {

  private static final Logger log = LoggerFactory.getLogger(EncryptedHandShakePacketDecoder.class);
  private final Crypto crypto;
  private final MultiMapper multiMapper;

  /**
   * Instantiates a new Encrypted hand shake packet decoder.
   *
   * @param crypto      the crypto
   * @param multiMapper the multi mapper
   */
  public EncryptedHandShakePacketDecoder(Crypto crypto, MultiMapper multiMapper) {
    this.crypto = crypto;
    this.multiMapper = multiMapper;
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, EncryptedHandShakePacket msg, List<Object> out) throws Exception {
    log.debug("{} decode", ctx.channel().toString());
    try {
      byte[] plainText = crypto.decrypt(msg.getBody().array());
      EncryptionMethod encryptionMethod = multiMapper.read(
        ReadCommand.of(EncodingType.of(msg.getHeader().getEncoding()), plainText, EncryptionMethod.class));
      out.add(HandShakePacket.builder()
        .header(msg.getHeader())
        .encryptionMethod(encryptionMethod)
        .build());
    } catch (Throwable e) {
      throw new HandShakeDecodeFailException(msg.getHeader(), e);
    }
  }
}
