package io.github.ppzxc.codec.encoder;

import io.github.ppzxc.codec.exception.MessageEncodeFailProblemException;
import io.github.ppzxc.codec.exception.SerializeFailedProblemException;
import io.github.ppzxc.codec.mapper.MultiMapper;
import io.github.ppzxc.codec.mapper.WriteCommand;
import io.github.ppzxc.codec.model.AbstractMessage;
import io.github.ppzxc.codec.model.EncodingType;
import io.github.ppzxc.codec.model.Header;
import io.github.ppzxc.codec.model.OutboundMessage;
import io.github.ppzxc.crypto.Crypto;
import io.github.ppzxc.crypto.CryptoException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Outbound message encoder.
 */
public class OutboundMessageEncoder extends MessageToMessageEncoder<OutboundMessage> {

  private static final Logger log = LoggerFactory.getLogger(OutboundMessageEncoder.class);
  private final Crypto crypto;
  private final MultiMapper multiMapper;

  /**
   * Instantiates a new Outbound message encoder.
   *
   * @param crypto      the crypto
   * @param multiMapper the multi mapper
   */
  public OutboundMessageEncoder(Crypto crypto, MultiMapper multiMapper) {
    this.crypto = crypto;
    this.multiMapper = multiMapper;
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, OutboundMessage msg, List<Object> out) throws Exception {
    log.debug("{} encode", ctx.channel());
    try {
      byte[] body = makeBody(msg);
      int bodyLength = body.length + Header.MINIMUM_BODY_LENGTH;
      ByteBuf buffer = Unpooled.buffer(bodyLength);
      buffer.writeInt(msg.header().getId());
      buffer.writeByte(msg.header().getType());
      buffer.writeByte(msg.header().getStatus());
      buffer.writeByte(msg.header().getEncoding());
      buffer.writeByte(msg.header().getReserved());
      buffer.writeInt(bodyLength);
      buffer.writeBytes(body);
      buffer.writeBytes(AbstractMessage.LINE_DELIMITER);
      out.add(buffer);
    } catch (Exception e) {
      throw new MessageEncodeFailProblemException(msg.header(), e);
    }
  }

  private byte[] makeBody(OutboundMessage msg) throws CryptoException, SerializeFailedProblemException {
    return msg.getBody() == null ? new byte[0]
      : crypto.encrypt(
        multiMapper.write(WriteCommand.of(EncodingType.of(msg.header().getEncoding()), msg.getBody())));
  }
}
