package io.github.ppzxc.codec.decoder;

import io.github.ppzxc.codec.exception.HandshakeException;
import io.github.ppzxc.codec.model.CodecProblemCode;
import io.github.ppzxc.codec.model.EncryptionMode;
import io.github.ppzxc.codec.model.EncryptionPadding;
import io.github.ppzxc.codec.model.EncryptionType;
import io.github.ppzxc.codec.model.HandshakeHeader;
import io.github.ppzxc.codec.model.HandshakeResult;
import io.github.ppzxc.codec.model.HandshakeType;
import io.github.ppzxc.codec.model.LineDelimiter;
import io.github.ppzxc.crypto.Crypto;
import io.github.ppzxc.fixh.ExceptionUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateHandler;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
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
      throw new HandshakeException(readableBytes, CodecProblemCode.SHORT_LENGTH);
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
      throw new HandshakeException(e, CodecProblemCode.DECRYPT_FAIL);
    }
    byte[] ivParameter = Arrays.copyOfRange(cipherText, 0, HandshakeHeader.IV_PARAMETER_LENGTH);
    byte[] symmetricKey = Arrays.copyOfRange(cipherText, HandshakeHeader.IV_PARAMETER_LENGTH, cipherText.length);

    // check body
    if (Arrays.stream(HandshakeHeader.AES_KEY_SIZES).noneMatch(aesKeySize -> aesKeySize == symmetricKey.length)) {
      throw new HandshakeException(symmetricKey.length, CodecProblemCode.INVALID_KEY_SIZE);
    }

    // make aes crypto
    Crypto aesCrypto;
    try {
      aesCrypto = getAesCrypto(handShakeHeader, ivParameter, symmetricKey);
    } catch (Exception e) {
      throw new HandshakeException(e, CodecProblemCode.CRYPTO_CREATE_FAIL);
    }

    ChannelPipeline pipeline = ctx.pipeline();
    addHandler(pipeline, aesCrypto);
    pipeline.remove(IdleStateHandler.class);
    pipeline.remove(HandshakeTimeoutStateHandler.class);
    pipeline.remove(this);
    ctx.channel().writeAndFlush(HandshakeResult.of(CodecProblemCode.OK));
  }

  private HandshakeHeader getHeaderAndValidation(ByteBuf msg) throws HandshakeException {
    // line delimiter
    if (!ByteBufUtil.equals(msg, msg.readableBytes() - 2, LineDelimiter.BYTE_BUF, 0, 2)) {
      throw new HandshakeException("null", CodecProblemCode.MISSING_LINE_DELIMITER);
    }
    // length
    int length = msg.readInt();
    if (length < HandshakeHeader.MINIMUM_LENGTH - HandshakeHeader.LENGTH_FIELD_LENGTH) {
      throw new HandshakeException(length, CodecProblemCode.SHORT_LENGTH_FIELD);
    }
    // handShake type
    byte rawHandShakeType = msg.readByte();
    HandshakeType handShakeType = HandshakeType.of(rawHandShakeType);
    if (handShakeType != HandshakeType.RSA_1024) {
      throw new HandshakeException(rawHandShakeType, CodecProblemCode.INVALID_HAND_SHAKE_TYPE);
    }
    // encryption type
    byte rawEncryptionType = msg.readByte();
    EncryptionType encryptionType = EncryptionType.of(rawEncryptionType);
    if (encryptionType != EncryptionType.ADVANCED_ENCRYPTION_STANDARD) {
      throw new HandshakeException(rawEncryptionType, CodecProblemCode.INVALID_ENCRYPTION_TYPE);
    }
    // encryption mode
    byte rawEncryptionMode = msg.readByte();
    EncryptionMode encryptionMode = EncryptionMode.of(rawEncryptionMode);
    if (encryptionMode != EncryptionMode.CIPHER_BLOCK_CHAINING) {
      throw new HandshakeException(rawEncryptionMode, CodecProblemCode.INVALID_ENCRYPTION_MODE);
    }
    // encryption padding
    byte rawEncryptionPadding = msg.readByte();
    EncryptionPadding encryptionPadding = EncryptionPadding.of(rawEncryptionPadding);
    if (encryptionPadding != EncryptionPadding.PKCS7PADDING) {
      throw new HandshakeException(rawEncryptionPadding, CodecProblemCode.INVALID_ENCRYPTION_PADDING);
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
    if (cause instanceof HandshakeException) {
      HandshakeException exception = (HandshakeException) cause;
      result = HandshakeResult.of(exception.getCodecProblemCode());
      log.info("{} id={} reject={} problem={} exception.message={}", ctx.channel(), exception.getId(),
        exception.getRejectedValue(), exception.getCodecProblemCode(), exception.getMessage());
    } else {
      Throwable findCause = ExceptionUtils.findCause(cause, HandshakeException.class);
      if (findCause instanceof HandshakeException) {
        HandshakeException exception = (HandshakeException) findCause;
        result = HandshakeResult.of(exception.getCodecProblemCode());
        log.info("{} id={} reject={} problem={} exception.message={}", ctx.channel(), exception.getId(),
          exception.getRejectedValue(), exception.getCodecProblemCode(), exception.getMessage());
      } else {
        result = HandshakeResult.of(CodecProblemCode.UNRECOGNIZED);
        log.info("{} id=0 reject=0 problem={} exception.message={}", ctx.channel(), CodecProblemCode.UNRECOGNIZED,
          cause.getMessage());
      }
    }

    ctx.channel()
      .writeAndFlush(result)
      .addListener(ignored -> ctx.executor().schedule(() -> ctx.close(), closeDelay, closeDelayTimeUnit));
  }
}
