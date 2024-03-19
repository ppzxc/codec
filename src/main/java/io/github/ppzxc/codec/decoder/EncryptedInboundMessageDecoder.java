package io.github.ppzxc.codec.decoder;

import io.github.ppzxc.codec.exception.BlankBodyException;
import io.github.ppzxc.codec.exception.CodecProblemException;
import io.github.ppzxc.codec.exception.DecryptFailException;
import io.github.ppzxc.codec.exception.InvalidLengthException;
import io.github.ppzxc.codec.exception.MissingLineDelimiterException;
import io.github.ppzxc.codec.exception.ShortLengthException;
import io.github.ppzxc.codec.model.CodecProblemCode;
import io.github.ppzxc.codec.model.Header;
import io.github.ppzxc.codec.model.InboundMessage;
import io.github.ppzxc.crypto.Crypto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptedInboundMessageDecoder extends MessageToMessageDecoder<ByteBuf> {

  private static final Logger log = LoggerFactory.getLogger(EncryptedInboundMessageDecoder.class);
  private final Crypto crypto;
  private final int minimumLength;

  public EncryptedInboundMessageDecoder(Crypto crypto, int minimumLength) {
    this.crypto = crypto;
    this.minimumLength = minimumLength;
  }

  public EncryptedInboundMessageDecoder(Crypto crypto) {
    this(crypto, Header.ID_FIELD_LENGTH + Header.LINE_DELIMITER_LENGTH);
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
    log.debug("{} id=[NO-ID] decode", ctx.channel());
    int length = preConditionAndGetLength(msg);
    ByteBuf decryptedPlainText = getDecryptedPlainText(msg);
    Header header = getHeader(length, decryptedPlainText);
    byte[] body = getBody(decryptedPlainText);
    out.add(InboundMessage.builder()
      .header(header)
      .body(body)
      .build());
  }

  private int preConditionAndGetLength(ByteBuf msg) throws CodecProblemException {
    int initialReadableBytes = msg.readableBytes();
    if (initialReadableBytes == 0) {
      throw new BlankBodyException("byte array require non null");
    }
    if (initialReadableBytes < minimumLength) {
      throw new ShortLengthException(initialReadableBytes, minimumLength);
    }
    if (!ByteBufUtil.equals(msg, msg.readableBytes() - 2, Header.LINE_DELIMITER_BYTE_BUF, 0, 2)) {
      throw new MissingLineDelimiterException();
    }
    int length = msg.readInt();
    int readableBytes = msg.readableBytes();
    if (length != readableBytes) {
      throw new InvalidLengthException(length, readableBytes);
    }
    return length;
  }

  private ByteBuf getDecryptedPlainText(ByteBuf msg) throws DecryptFailException {
    byte[] cipherText = new byte[msg.readableBytes()];
    msg.readBytes(cipherText);
    try {
      return Unpooled.wrappedBuffer(crypto.decrypt(cipherText));
    } catch (Exception e) {
      throw new DecryptFailException(e, CodecProblemCode.DECRYPT_FAIL);
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
