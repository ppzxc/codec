package io.github.ppzxc.codec.decoder;

import io.github.ppzxc.codec.Constants.LineDelimiter;
import io.github.ppzxc.codec.exception.BlankBodyCodecException;
import io.github.ppzxc.codec.exception.CodecException;
import io.github.ppzxc.codec.exception.DecryptCodecException;
import io.github.ppzxc.codec.exception.InvalidLengthCodecException;
import io.github.ppzxc.codec.exception.MissingLineDelimiterCodecException;
import io.github.ppzxc.codec.exception.ShortLengthCodecException;
import io.github.ppzxc.codec.model.CodecCode;
import io.github.ppzxc.codec.model.Header;
import io.github.ppzxc.codec.model.InboundProtocol;
import io.github.ppzxc.crypto.Crypto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ByteBufDecoder extends MessageToMessageDecoder<ByteBuf> {

  private static final Logger log = LoggerFactory.getLogger(ByteBufDecoder.class);
  private final Crypto crypto;
  private final int minimumReadableBytes;

  public ByteBufDecoder(Crypto crypto, int minimumReadableBytes) {
    this.crypto = crypto;
    this.minimumReadableBytes = minimumReadableBytes;
  }

  public ByteBufDecoder(Crypto crypto) {
    this(crypto, Header.MINIMUM_LENGTH);
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
    log.debug("{} id=[NO-ID] decode", ctx.channel());
    int length = preConditionAndGetLength(msg);
    ByteBuf decryptedPlainText = getDecryptedPlainText(msg);
    Header header = getHeader(length, decryptedPlainText);
    log.debug("{} id={} header={} decode", ctx.channel(), header.getId(), header);
    byte[] body = getBody(decryptedPlainText);
    out.add(InboundProtocol.builder()
      .header(header)
      .body(body)
      .build());
  }

  private int preConditionAndGetLength(ByteBuf msg) throws CodecException {
    int initialReadableBytes = msg.readableBytes();
    if (initialReadableBytes == 0) {
      throw new BlankBodyCodecException("byte array require non null");
    }
    if (initialReadableBytes < minimumReadableBytes) {
      throw new ShortLengthCodecException(initialReadableBytes, minimumReadableBytes);
    }
    if (!ByteBufUtil.equals(msg, msg.readableBytes() - 2, LineDelimiter.BYTE_BUF, 0, 2)) {
      throw new MissingLineDelimiterCodecException();
    }
    int length = msg.readInt();
    int readableBytes = msg.readableBytes();
    if (length != readableBytes) {
      throw new InvalidLengthCodecException(length, readableBytes);
    }
    return length;
  }

  private ByteBuf getDecryptedPlainText(ByteBuf msg) throws DecryptCodecException {
    byte[] cipherText = new byte[msg.readableBytes()];
    msg.readBytes(cipherText);
    try {
      return Unpooled.wrappedBuffer(crypto.decrypt(cipherText));
    } catch (Exception e) {
      throw new DecryptCodecException(e, CodecCode.DECRYPT_FAIL);
    }
  }

  private Header getHeader(int length, ByteBuf decryptedPlainText) {
    return Header.builder()
      .length(length)
      .id(decryptedPlainText.readLong())
      .type(decryptedPlainText.readByte())
      .status(decryptedPlainText.readByte())
      .encoding(decryptedPlainText.readByte())
      .reserved(decryptedPlainText.readByte())
      .build();
  }

  private static byte[] getBody(ByteBuf decryptedPlainText) {
    byte[] body = new byte[decryptedPlainText.readableBytes()];
    decryptedPlainText.readBytes(body);
    return body;
  }
}
