package io.github.ppzxc.codec.decoder;

import io.github.ppzxc.codec.exception.HandShakeDecodeFailException;
import io.github.ppzxc.codec.mapper.MultiMapper;
import io.github.ppzxc.codec.mapper.ReadCommand;
import io.github.ppzxc.codec.model.EncodingType;
import io.github.ppzxc.codec.model.EncryptedHandShakeMessage;
import io.github.ppzxc.codec.model.EncryptionMethod;
import io.github.ppzxc.codec.model.HandShakeMessage;
import io.github.ppzxc.crypto.Crypto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Encrypted hand shake message decoder.
 */
public class EncryptedHandShakeMessageDecoder extends MessageToMessageDecoder<EncryptedHandShakeMessage> {

  private static final Logger log = LoggerFactory.getLogger(EncryptedHandShakeMessageDecoder.class);
  private final Crypto crypto;
  private final MultiMapper multiMapper;

  /**
   * Instantiates a new Encrypted hand shake message decoder.
   *
   * @param crypto the crypto
   * @param multiMapper the multi mapper
   */
  public EncryptedHandShakeMessageDecoder(Crypto crypto, MultiMapper multiMapper) {
    this.crypto = crypto;
    this.multiMapper = multiMapper;
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, EncryptedHandShakeMessage msg, List<Object> out) throws Exception {
    log.debug("{} decode", ctx.channel());
    try {
      byte[] plainText = crypto.decrypt(msg.getBody().array());
      EncryptionMethod encryptionMethod = multiMapper.read(
        ReadCommand.of(EncodingType.of(msg.header().getEncoding()), plainText, EncryptionMethod.class));
      out.add(HandShakeMessage.builder()
        .header(msg.header())
        .encryptionMethod(encryptionMethod)
        .build());
    } catch (Exception e) {
      throw new HandShakeDecodeFailException(msg.header(), e);
    }
  }
}
