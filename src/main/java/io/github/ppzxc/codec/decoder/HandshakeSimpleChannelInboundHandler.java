package io.github.ppzxc.codec.decoder;

import io.github.ppzxc.codec.Constants;
import io.github.ppzxc.codec.Constants.CodecNames;
import io.github.ppzxc.codec.Constants.LineDelimiter;
import io.github.ppzxc.codec.exception.HandshakeCodecException;
import io.github.ppzxc.codec.model.CodecCode;
import io.github.ppzxc.codec.model.EncryptionMode;
import io.github.ppzxc.codec.model.EncryptionPadding;
import io.github.ppzxc.codec.model.EncryptionType;
import io.github.ppzxc.codec.model.HandshakeHeader;
import io.github.ppzxc.codec.model.HandshakeResult;
import io.github.ppzxc.codec.model.HandshakeType;
import io.github.ppzxc.crypto.Crypto;
import io.github.ppzxc.fixh.ExceptionUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HandshakeSimpleChannelInboundHandler extends SimpleChannelInboundHandler<ByteBuf> {

  private static final Logger log = LoggerFactory.getLogger(HandshakeSimpleChannelInboundHandler.class);
  private final Crypto rsaCrypto;
  private final long closeDelay;
  private final TimeUnit closeDelayTimeUnit;

  public HandshakeSimpleChannelInboundHandler(Crypto rsaCrypto, long closeDelay, TimeUnit closeDelayTimeUnit) {
    this.rsaCrypto = rsaCrypto;
    this.closeDelay = closeDelay;
    this.closeDelayTimeUnit = closeDelayTimeUnit;
  }

  protected HandshakeSimpleChannelInboundHandler(Crypto rsaCrypto) {
    this(rsaCrypto, 1, TimeUnit.SECONDS);
  }

  public abstract Crypto getAesCrypto(HandshakeHeader handShakeHeader, byte[] ivParameter, byte[] symmetricKey);

  public abstract void addHandler(ChannelPipeline pipeline, Crypto crypto);

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
    log.debug("{} id=[NO-ID] decode", ctx.channel());
    // check corrupted length
    int readableBytes = msg.readableBytes();
    if (readableBytes < HandshakeHeader.MINIMUM_LENGTH) {
      throw new HandshakeCodecException(readableBytes, CodecCode.SHORT_LENGTH);
    }

    // check field
    HandshakeHeader handShakeHeader = getHeaderAndValidation(msg);

    // decrypt body
    byte[] encryptedHandShakeBody = new byte[msg.readableBytes()];
    msg.readBytes(encryptedHandShakeBody);
    byte[] cipherText;
    try {
      cipherText = rsaCrypto.decrypt(encryptedHandShakeBody);
    } catch (Exception e) {
      throw new HandshakeCodecException(e, CodecCode.DECRYPT_FAIL);
    }
    byte[] ivParameter = Arrays.copyOfRange(cipherText, 0, HandshakeHeader.IV_PARAMETER_LENGTH);
    byte[] symmetricKey = Arrays.copyOfRange(cipherText, HandshakeHeader.IV_PARAMETER_LENGTH, cipherText.length);

    // check body
    if (Arrays.stream(Constants.Crypto.SYMMETRIC_KEY_SIZE).noneMatch(aesKeySize -> aesKeySize == symmetricKey.length)) {
      throw new HandshakeCodecException(symmetricKey.length, CodecCode.INVALID_KEY_SIZE);
    }

    // make aes crypto
    Crypto aesCrypto;
    try {
      aesCrypto = getAesCrypto(handShakeHeader, ivParameter, symmetricKey);
    } catch (Exception e) {
      throw new HandshakeCodecException(e, CodecCode.CRYPTO_CREATE_FAIL);
    }

    log.debug("{} id=[NO-ID] header={} iv.length={} key.length={} iv=[{}] symmetric=[{}] message=handshake success",
      ctx.channel(), handShakeHeader, ivParameter.length, symmetricKey.length,
      new String(ivParameter, StandardCharsets.UTF_8), new String(symmetricKey, StandardCharsets.UTF_8));

    ChannelPipeline pipeline = ctx.pipeline();
    addHandler(pipeline, aesCrypto);
    pipeline.names().stream()
      .filter(CodecNames.HANDSHAKES::contains)
      .collect(Collectors.toList())
      .forEach(pipeline::remove);
    ctx.channel().writeAndFlush(HandshakeResult.of(CodecCode.OK));
  }

  private HandshakeHeader getHeaderAndValidation(ByteBuf msg) throws HandshakeCodecException {
    // line delimiter
    if (!ByteBufUtil.equals(msg, msg.readableBytes() - 2, LineDelimiter.BYTE_BUF, 0, 2)) {
      throw new HandshakeCodecException("null", CodecCode.MISSING_LINE_DELIMITER);
    }
    // length
    int length = msg.readInt();
    if (length < HandshakeHeader.MINIMUM_LENGTH - HandshakeHeader.LENGTH_FIELD_LENGTH) {
      throw new HandshakeCodecException(length, CodecCode.SHORT_LENGTH_FIELD);
    }
    // handShake type
    byte rawHandShakeType = msg.readByte();
    HandshakeType handShakeType = HandshakeType.of(rawHandShakeType);
    if (handShakeType != HandshakeType.RSA_1024) {
      throw new HandshakeCodecException(rawHandShakeType, CodecCode.INVALID_HAND_SHAKE_TYPE);
    }
    // encryption type
    byte rawEncryptionType = msg.readByte();
    EncryptionType encryptionType = EncryptionType.of(rawEncryptionType);
    if (encryptionType != EncryptionType.ADVANCED_ENCRYPTION_STANDARD) {
      throw new HandshakeCodecException(rawEncryptionType, CodecCode.INVALID_ENCRYPTION_TYPE);
    }
    // encryption mode
    byte rawEncryptionMode = msg.readByte();
    EncryptionMode encryptionMode = EncryptionMode.of(rawEncryptionMode);
    if (encryptionMode != EncryptionMode.CIPHER_BLOCK_CHAINING) {
      throw new HandshakeCodecException(rawEncryptionMode, CodecCode.INVALID_ENCRYPTION_MODE);
    }
    // encryption padding
    byte rawEncryptionPadding = msg.readByte();
    EncryptionPadding encryptionPadding = EncryptionPadding.of(rawEncryptionPadding);
    if (encryptionPadding != EncryptionPadding.PKCS7PADDING) {
      throw new HandshakeCodecException(rawEncryptionPadding, CodecCode.INVALID_ENCRYPTION_PADDING);
    }
    return HandshakeHeader.builder()
      .length(length)
      .handShakeType(handShakeType)
      .encryptionType(encryptionType)
      .encryptionMode(encryptionMode)
      .encryptionPadding(encryptionPadding)
      .build();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    ByteBuf result;
    if (cause instanceof HandshakeCodecException) {
      HandshakeCodecException exception = (HandshakeCodecException) cause;
      result = HandshakeResult.of(exception.getCodecCode());
      log.info("{} id={} reject={} code={} exception.message={}", ctx.channel(), exception.getId(),
        exception.getRejectedValue(), exception.getCodecCode(), exception.getMessage());
    } else {
      Throwable findCause = ExceptionUtils.findCause(cause, HandshakeCodecException.class);
      if (findCause instanceof HandshakeCodecException) {
        HandshakeCodecException exception = (HandshakeCodecException) findCause;
        result = HandshakeResult.of(exception.getCodecCode());
        log.info("{} id={} reject={} code={} exception.message={}", ctx.channel(), exception.getId(),
          exception.getRejectedValue(), exception.getCodecCode(), exception.getMessage());
      } else {
        result = HandshakeResult.of(CodecCode.UNRECOGNIZED);
        log.info("{} id=0 reject=0 code={} exception.message={}", ctx.channel(), CodecCode.UNRECOGNIZED,
          cause.getMessage());
      }
    }

    ctx.channel()
      .writeAndFlush(result)
      .addListener(ignored -> ctx.executor().schedule(() -> ctx.close(), closeDelay, closeDelayTimeUnit));
  }
}
