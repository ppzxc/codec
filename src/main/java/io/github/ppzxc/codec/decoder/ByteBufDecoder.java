package io.github.ppzxc.codec.decoder;

import io.github.ppzxc.codec.exception.LessThanMinimumMessageLengthCodeException;
import io.github.ppzxc.codec.exception.MissingLineDelimiterCodeException;
import io.github.ppzxc.codec.exception.NotSameLengthCodeException;
import io.github.ppzxc.codec.exception.NotSupportedBodyLengthException;
import io.github.ppzxc.codec.exception.NullPointerCodeException;
import io.github.ppzxc.codec.exception.ProblemCodeException;
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

/**
 * The type Byte buf decoder.
 */
public class ByteBufDecoder extends MessageToMessageDecoder<ByteBuf> {

  private static final Logger log = LoggerFactory.getLogger(ByteBufDecoder.class);
  private final int maximumBodyLength;

  /**
   * Instantiates a new Byte buf decoder.
   *
   * @param maximumBodyLength the maximum body length
   */
  public ByteBufDecoder(int maximumBodyLength) {
    this.maximumBodyLength = maximumBodyLength;
  }

  /**
   * Instantiates a new Byte buf decoder.
   */
  public ByteBufDecoder() {
    this(1024 * 1024 * 4); // 4 mb, 4 mega bytes
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
    log.debug("{} decode", ctx.channel());
    preCondition(msg);
    Header header = Header.builder()
      .id(msg.readInt())
      .type(msg.readByte())
      .status(msg.readByte())
      .encoding(msg.readByte())
      .reserved(msg.readByte())
      .bodyLength(msg.readInt())
      .build();
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

  private void preCondition(ByteBuf msg) throws ProblemCodeException {
    if (msg.readableBytes() <= 0) {
      throw new NullPointerCodeException("byte array require non null");
    }
    if (msg.readableBytes() < AbstractMessage.MINIMUM_MESSAGE_LENGTH) {
      throw new LessThanMinimumMessageLengthCodeException(
        msg.readableBytes() + " less than " + AbstractMessage.MINIMUM_MESSAGE_LENGTH);
    }
  }

  private ByteBuf getBody(Header header, ByteBuf msg) throws ProblemCodeException {
    if (header.getBodyLength() > maximumBodyLength || msg.readableBytes() > maximumBodyLength) {
      throw new NotSupportedBodyLengthException(header);
    }
    if (header.getBodyLength() != msg.readableBytes()) {
      throw new NotSameLengthCodeException(header, msg.readableBytes());
    }
    return msg.readBytes(msg.readableBytes());
  }

  private void postCondition(Header header, ByteBuf body) throws ProblemCodeException {
    if (isNotContainsLineDelimiter(body)) {
      throw new MissingLineDelimiterCodeException(header);
    }
  }

  private boolean isNotContainsLineDelimiter(ByteBuf body) {
    return !ByteBufUtil.equals(body, body.readableBytes() - Header.MINIMUM_BODY_LENGTH,
      Unpooled.wrappedBuffer(new byte[]{'\r', '\n'}), 0, Header.MINIMUM_BODY_LENGTH);
  }
}
