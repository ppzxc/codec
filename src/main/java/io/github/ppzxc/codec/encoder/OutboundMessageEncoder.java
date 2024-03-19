package io.github.ppzxc.codec.encoder;

import io.github.ppzxc.codec.exception.OutboundMessageEncoderException;
import io.github.ppzxc.codec.exception.SerializeFailedException;
import io.github.ppzxc.codec.mapper.Mapper;
import io.github.ppzxc.codec.mapper.WriteCommand;
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

public class OutboundMessageEncoder extends MessageToMessageEncoder<OutboundMessage> {

  private static final Logger log = LoggerFactory.getLogger(OutboundMessageEncoder.class);
  private final Crypto crypto;
  private final Mapper mapper;

  public OutboundMessageEncoder(Crypto crypto, Mapper mapper) {
    this.crypto = crypto;
    this.mapper = mapper;
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, OutboundMessage msg, List<Object> out) throws Exception {
    log.debug("{} id={} encode", ctx.channel(), msg.header().getId());
    try {
      byte[] body = getBody(msg);
      int bodyLength = body.length + Header.LINE_DELIMITER_LENGTH;
      ByteBuf buffer = Unpooled.buffer(Header.ID_FIELD_LENGTH + bodyLength);
      buffer.writeInt(bodyLength);
      buffer.writeLong(msg.header().getId());
      buffer.writeByte(msg.header().getType());
      buffer.writeByte(msg.header().getStatus());
      buffer.writeByte(msg.header().getEncoding());
      buffer.writeByte(msg.header().getReserved());
      buffer.writeBytes(body);
      buffer.writeBytes(Header.LINE_DELIMITER);
      out.add(buffer);
    } catch (Exception e) {
      throw new OutboundMessageEncoderException(msg.header(), e);
    }
  }

  private byte[] getBody(OutboundMessage msg) throws CryptoException, SerializeFailedException {
    if (msg.body() == null) {
      return new byte[0];
    }
    byte[] plainText = mapper.write(WriteCommand.of(EncodingType.of(msg.header().getEncoding()), msg.body()));
    return crypto.encrypt(plainText);
  }
}
