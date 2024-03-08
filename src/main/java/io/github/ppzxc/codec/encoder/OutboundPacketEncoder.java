package io.github.ppzxc.codec.encoder;

import io.github.ppzxc.codec.exception.OutboundPacketEncodeFailException;
import io.github.ppzxc.codec.exception.SerializeFailedException;
import io.github.ppzxc.codec.mapper.MultiMapper;
import io.github.ppzxc.codec.mapper.WriteCommand;
import io.github.ppzxc.codec.model.AbstractRawPacket;
import io.github.ppzxc.codec.model.EncodingType;
import io.github.ppzxc.codec.model.Header;
import io.github.ppzxc.codec.model.PrepareOutboundPacket;
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
 * The type Outbound packet encoder.
 */
public class OutboundPacketEncoder extends MessageToMessageEncoder<PrepareOutboundPacket> {

  private static final Logger log = LoggerFactory.getLogger(OutboundPacketEncoder.class);
  private final Crypto crypto;
  private final MultiMapper multiMapper;

  /**
   * Instantiates a new Outbound packet encoder.
   *
   * @param crypto      the crypto
   * @param multiMapper the multi mapper
   */
  public OutboundPacketEncoder(Crypto crypto, MultiMapper multiMapper) {
    this.crypto = crypto;
    this.multiMapper = multiMapper;
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, PrepareOutboundPacket msg, List<Object> out) throws Exception {
    log.debug("{} encode", ctx.channel().toString());
    try {
      byte[] body = makeBody(msg);
      int bodyLength = body.length + Header.MINIMUM_BODY_LENGTH;
      ByteBuf outboundPacket = Unpooled.buffer(bodyLength);
      outboundPacket.writeInt(msg.getHeader().getId());
      outboundPacket.writeByte(msg.getHeader().getType());
      outboundPacket.writeByte(msg.getHeader().getStatus());
      outboundPacket.writeByte(msg.getHeader().getEncoding());
      outboundPacket.writeByte(msg.getHeader().getReserved());
      outboundPacket.writeInt(bodyLength);
      outboundPacket.writeBytes(body);
      outboundPacket.writeBytes(AbstractRawPacket.LINE_DELIMITER);
      out.add(outboundPacket);
    } catch (Throwable throwable) {
      throw new OutboundPacketEncodeFailException(msg.getHeader(), throwable);
    }
  }

  private byte[] makeBody(PrepareOutboundPacket msg) throws CryptoException, SerializeFailedException {
    return msg.getBody() == null ? new byte[0]
      : crypto.encrypt(
        multiMapper.write(WriteCommand.of(EncodingType.of(msg.getHeader().getEncoding()), msg.getBody())));
  }
}
