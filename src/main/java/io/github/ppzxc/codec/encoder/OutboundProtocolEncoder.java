package io.github.ppzxc.codec.encoder;

import io.github.ppzxc.codec.Constants.LineDelimiter;
import io.github.ppzxc.codec.exception.OutboundCodecException;
import io.github.ppzxc.codec.exception.SerializeException;
import io.github.ppzxc.codec.mapper.Mapper;
import io.github.ppzxc.codec.mapper.WriteCommand;
import io.github.ppzxc.codec.model.EncodingType;
import io.github.ppzxc.codec.model.Header;
import io.github.ppzxc.codec.model.OutboundProtocol;
import io.github.ppzxc.crypto.Crypto;
import io.github.ppzxc.crypto.CryptoException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutboundProtocolEncoder extends MessageToMessageEncoder<OutboundProtocol> {

  private static final Logger log = LoggerFactory.getLogger(OutboundProtocolEncoder.class);
  private final Crypto crypto;
  private final Mapper mapper;

  public OutboundProtocolEncoder(Crypto crypto, Mapper mapper) {
    this.crypto = crypto;
    this.mapper = mapper;
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, OutboundProtocol msg, List<Object> out) throws Exception {
    log.debug("{} id={} encode", ctx.channel(), msg.header().getId());
    try {
      byte[] encryptedPayload = getEncryptedPayload(msg);
      ByteBuf buffer = Unpooled.buffer(Header.LENGTH_FIELD_LENGTH + encryptedPayload.length + LineDelimiter.LENGTH);
      buffer.writeInt(encryptedPayload.length + LineDelimiter.LENGTH);
      buffer.writeBytes(encryptedPayload);
      buffer.writeBytes(LineDelimiter.BYTE_ARRAY);
      out.add(buffer);
    } catch (Exception e) {
      throw new OutboundCodecException(msg.header(), e);
    }
  }

  private byte[] getEncryptedPayload(OutboundProtocol msg) throws SerializeException, CryptoException {
    byte[] mappedBody = getMappedBody(msg);
    ByteBuf plainText = Unpooled.buffer(Header.ID_FIELD_LENGTH + Header.PROTOCOL_FIELDS_LENGTH + mappedBody.length);
    plainText.writeLong(msg.header().getId());
    plainText.writeByte(msg.header().getType());
    plainText.writeByte(msg.header().getStatus());
    plainText.writeByte(msg.header().getEncoding());
    plainText.writeByte(msg.header().getReserved());
    plainText.writeBytes(mappedBody);
    return crypto.encrypt(plainText.array());
  }

  private byte[] getMappedBody(OutboundProtocol msg) throws SerializeException {
    if (msg.body() == null) {
      return new byte[0];
    }
    return mapper.write(WriteCommand.of(EncodingType.of(msg.header().getEncoding()), msg.body()));
  }
}
