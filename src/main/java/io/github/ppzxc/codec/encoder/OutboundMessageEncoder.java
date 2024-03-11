package io.github.ppzxc.codec.encoder;

import io.github.ppzxc.codec.exception.MessageEncodeFailException;
import io.github.ppzxc.codec.exception.SerializeFailedException;
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
   * @param crypto the crypto
   * @param multiMapper the multi mapper
   */
  public OutboundMessageEncoder(Crypto crypto, MultiMapper multiMapper) {
    this.crypto = crypto;
    this.multiMapper = multiMapper;
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, OutboundMessage msg, List<Object> out) throws Exception {
    log.debug("{} encode", ctx.channel().toString());
    try {
      byte[] body = makeBody(msg);
      int bodyLength = body.length + Header.MINIMUM_BODY_LENGTH;
      ByteBuf buffer = Unpooled.buffer(bodyLength);
      buffer.writeInt(msg.getHeader().getId());
      buffer.writeByte(msg.getHeader().getType());
      buffer.writeByte(msg.getHeader().getStatus());
      buffer.writeByte(msg.getHeader().getEncoding());
      buffer.writeByte(msg.getHeader().getReserved());
      buffer.writeInt(bodyLength);
      buffer.writeBytes(body);
      buffer.writeBytes(AbstractMessage.LINE_DELIMITER);
      out.add(buffer);
    } catch (Throwable throwable) {
      throw new MessageEncodeFailException(msg.getHeader(), throwable);
    }
  }

  private byte[] makeBody(OutboundMessage msg) throws CryptoException, SerializeFailedException {
    return msg.getBody() == null ? new byte[0]
      : crypto.encrypt(
        multiMapper.write(WriteCommand.of(EncodingType.of(msg.getHeader().getEncoding()), msg.getBody())));
  }
}
