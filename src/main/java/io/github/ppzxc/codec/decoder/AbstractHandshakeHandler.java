package io.github.ppzxc.codec.decoder;

import io.github.ppzxc.codec.exception.HandshakeException;
import io.github.ppzxc.codec.model.CodecProblemCode;
import io.github.ppzxc.codec.model.EncryptionMode;
import io.github.ppzxc.codec.model.EncryptionPadding;
import io.github.ppzxc.codec.model.EncryptionType;
import io.github.ppzxc.codec.model.HandshakeHeader;
import io.github.ppzxc.codec.model.HandshakeType;
import io.github.ppzxc.crypto.Crypto;
import io.github.ppzxc.fixh.ExceptionUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ReferenceCountUtil;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractHandshakeHandler extends ByteToMessageDecoder implements ChannelOutboundHandler {

  private static final Logger log = LoggerFactory.getLogger(AbstractHandshakeHandler.class);
  private final Crypto rsaCrypto;

  protected AbstractHandshakeHandler(Crypto rsaCrypto) {
    this.rsaCrypto = rsaCrypto;
  }

  public abstract Crypto getAesCrypto(HandshakeHeader handShakeHeader, byte[] ivParameter, byte[] symmetricKey)
    throws Exception;

  public abstract void addHandler(ChannelPipeline pipeline, Crypto crypto);

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
    try {
      log.debug("{} id=[NO-ID] decode", ctx.channel());
      // check corrupted length
      int readableBytes = msg.readableBytes();
      if (readableBytes < HandshakeHeader.MINIMUM_LENGTH) {
        throw new HandshakeException(readableBytes, CodecProblemCode.SHORT_LENGTH);
      }

      // check field
      HandshakeHeader handShakeHeader = validateHeader(msg);

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
        throw new HandshakeException("rejected: key " + symmetricKey.length, CodecProblemCode.INVALID_KEY_SIZE);
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
      pipeline.remove(this);
      ctx.channel().writeAndFlush(createResult(CodecProblemCode.OK));
    } catch (Exception e) {
      ReferenceCountUtil.release(msg);
      throw e;
    }
  }

  private HandshakeHeader validateHeader(ByteBuf in) throws HandshakeException {
    // length
    int length = in.readInt();
    if (length < HandshakeHeader.MINIMUM_LENGTH - HandshakeHeader.LENGTH_FIELD_LENGTH) {
      throw new HandshakeException(length, CodecProblemCode.SHORT_LENGTH_FIELD);
    }
    // handShake type
    byte rawHandShakeType = in.readByte();
    HandshakeType handShakeType = HandshakeType.of(rawHandShakeType);
    if (handShakeType != HandshakeType.RSA_1024) {
      throw new HandshakeException(rawHandShakeType, CodecProblemCode.INVALID_HAND_SHAKE_TYPE);
    }
    // encryption type
    byte rawEncryptionType = in.readByte();
    EncryptionType encryptionType = EncryptionType.of(rawEncryptionType);
    if (encryptionType != EncryptionType.ADVANCED_ENCRYPTION_STANDARD) {
      throw new HandshakeException(rawEncryptionType, CodecProblemCode.INVALID_ENCRYPTION_TYPE);
    }
    // encryption mode
    byte rawEncryptionMode = in.readByte();
    EncryptionMode encryptionMode = EncryptionMode.of(rawEncryptionMode);
    if (encryptionMode != EncryptionMode.CIPHER_BLOCK_CHAINING) {
      throw new HandshakeException(rawEncryptionMode, CodecProblemCode.INVALID_ENCRYPTION_MODE);
    }
    // encryption padding
    byte rawEncryptionPadding = in.readByte();
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
      result = createResult(((HandshakeException) cause).getCodecProblemCode());
    } else {
      Throwable findCause = ExceptionUtils.findCause(cause, HandshakeException.class);
      if (findCause instanceof HandshakeException) {
        result = createResult(((HandshakeException) findCause).getCodecProblemCode());
      } else {
        result = createResult(CodecProblemCode.UNRECOGNIZED);
      }
    }

    ctx.channel()
      .writeAndFlush(result)
      .addListener((ChannelFutureListener) future -> future.channel().close());
  }

  private ByteBuf createResult(CodecProblemCode unrecognized) {
    ByteBuf buffer = Unpooled.buffer(HandshakeHeader.LENGTH_FIELD_LENGTH + 1);
    buffer.writeInt(1);
    buffer.writeByte(unrecognized.getCode());
    return buffer;
  }

  @Override
  public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
    ctx.bind(localAddress, promise);
  }

  @Override
  public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress,
    ChannelPromise promise) throws Exception {
    ctx.connect(remoteAddress, localAddress, promise);
  }

  @Override
  public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
    ctx.disconnect();
  }

  @Override
  public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
    ctx.close();
  }

  @Override
  public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
    ctx.deregister(promise);
  }

  @Override
  public void read(ChannelHandlerContext ctx) throws Exception {
    ctx.read();
  }

  @Override
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
    throws Exception {
    ctx.write(msg);
//    if (msg instanceof ByteBuf) {
//      ctx.write(msg).addListener(future -> ReferenceCountUtil.safeRelease(msg));
//    } else {
//      UnsupportedMessageTypeException exception = new UnsupportedMessageTypeException(ByteBuf.class);
//      ReferenceCountUtil.safeRelease(msg);
//      promise.setFailure(exception);
//    }
  }

  @Override
  public void flush(ChannelHandlerContext ctx) throws Exception {
    ctx.flush();
  }
}
