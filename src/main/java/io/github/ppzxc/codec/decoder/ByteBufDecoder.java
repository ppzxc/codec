package io.github.ppzxc.codec.decoder;

import io.github.ppzxc.codec.exception.CodecProblemException;
import io.github.ppzxc.codec.exception.MinimumLengthLimitException;
import io.github.ppzxc.codec.exception.MissingDelimiterException;
import io.github.ppzxc.codec.exception.NotEqualLengthException;
import io.github.ppzxc.codec.exception.UnsupportedBodyLengthException;
import io.github.ppzxc.codec.exception.BlankBodyException;
import io.github.ppzxc.codec.model.AbstractMessage;
import io.github.ppzxc.codec.model.EncryptedHandShakeMessage;
import io.github.ppzxc.codec.model.HandShakeMessage;
import io.github.ppzxc.codec.model.Header;
import io.github.ppzxc.codec.model.InboundMessage;
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
  private final int maximumBodyLength;

  public ByteBufDecoder(int maximumBodyLength) {
    this.maximumBodyLength = maximumBodyLength;
  }

  public ByteBufDecoder() {
    this(1024 * 1024 * 4); // 4 mb, 4 mega bytes
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
    log.debug("{} id=[BEFORE DECODE] decode", ctx.channel());
    preCondition(msg);
    Header header = Header.builder()
      .id(msg.readInt())
      .type(msg.readByte())
      .status(msg.readByte())
      .encoding(msg.readByte())
      .reserved(msg.readByte())
      .bodyLength(msg.readInt())
      .build();
    log.debug("{} id={} decode", ctx.channel(), header.getId());
    ByteBuf body = getBody(header, msg);
    postCondition(header, body);
    if (header.getType() == HandShakeMessage.HEADER_TYPE_CODE) {
      out.add(EncryptedHandShakeMessage.builder()
        .header(header)
        .body(body)
        .build());
    } else {
      out.add(InboundMessage.builder()
        .header(header)
        .body(body)
        .build());
    }
  }

  private void preCondition(ByteBuf msg) throws CodecProblemException {
    if (msg.readableBytes() <= 0) {
      throw new BlankBodyException("byte array require non null");
    }
    if (msg.readableBytes() < AbstractMessage.MINIMUM_MESSAGE_LENGTH) {
      throw new MinimumLengthLimitException(
        msg.readableBytes() + " less than " + AbstractMessage.MINIMUM_MESSAGE_LENGTH);
    }
  }

  private ByteBuf getBody(Header header, ByteBuf msg) throws CodecProblemException {
    if (header.getBodyLength() > maximumBodyLength) {
      throw new UnsupportedBodyLengthException(header, String.valueOf(header.getBodyLength()));
    }
    if (msg.readableBytes() > maximumBodyLength) {
      throw new UnsupportedBodyLengthException(header, String.valueOf(msg.readableBytes()));
    }
    if (header.getBodyLength() != msg.readableBytes()) {
      throw new NotEqualLengthException(header, String.valueOf(msg.readableBytes()));
    }
    return msg.readBytes(msg.readableBytes());
  }

  private void postCondition(Header header, ByteBuf body) throws CodecProblemException {
    if (isNotContainsLineDelimiter(body)) {
      throw new MissingDelimiterException(header);
    }
  }

  private boolean isNotContainsLineDelimiter(ByteBuf body) {
    return !ByteBufUtil.equals(body, body.readableBytes() - Header.MINIMUM_BODY_LENGTH,
      Unpooled.wrappedBuffer(new byte[]{'\r', '\n'}), 0, Header.MINIMUM_BODY_LENGTH);
  }
}
