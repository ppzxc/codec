package io.github.ppzxc.codec.decoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.ppzxc.codec.Constants.CodecNames;
import io.github.ppzxc.codec.exception.HandshakeCodecException;
import io.github.ppzxc.codec.model.ByteArrayFixture;
import io.github.ppzxc.codec.model.CodecCode;
import io.github.ppzxc.codec.model.HandshakeFixture;
import io.github.ppzxc.codec.model.HandshakeHeader;
import io.github.ppzxc.codec.model.HandshakeResult;
import io.github.ppzxc.codec.model.InboundMessageFixture;
import io.github.ppzxc.codec.model.InboundProtocol;
import io.github.ppzxc.crypto.Crypto;
import io.github.ppzxc.crypto.CryptoException;
import io.github.ppzxc.fixh.ByteArrayUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.timeout.IdleStateHandler;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class HandshakeSimpleChannelInboundHandlerTest {

  private Crypto rsaCrypto;
  private Crypto aesCrypto;
  private EmbeddedChannel channel;

  @BeforeEach
  void setUp() {
    rsaCrypto = mock(Crypto.class);
    aesCrypto = mock(Crypto.class);
    channel = new EmbeddedChannel();
    channel.pipeline().addLast(CodecNames.HANDSHAKE_IDLE_HANDLER, new IdleStateHandler(3, 2, 1));
    channel.pipeline()
      .addLast(CodecNames.HANDSHAKE_TIMEOUT_HANDLER, new HandshakeTimeoutStateHandler(1, TimeUnit.SECONDS));
    channel.pipeline().addLast(CodecNames.HANDSHAKE_CHANNEL_HANDLER, getHandler());
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3})
  void should_return_SHORT_LENGTH(int value) {
    // given
    byte[] given = ByteArrayFixture.create(ByteArrayUtils.giveMeOne(value));

    // when
    channel.writeInbound(Unpooled.wrappedBuffer(given));
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeResult.LENGTH);
    assertThat(actual.readInt()).isEqualTo(HandshakeResult.BODY_LENGTH);
    assertThat(actual.readByte()).isEqualTo(CodecCode.SHORT_LENGTH.getCode());
  }

  @Test
  void should_return_MISSING_LINE_DELIMITER() {
    // given
    ByteBuf given = HandshakeFixture.missingLineDelimiter(ByteArrayUtils.giveMeOne(128));

    // when
    channel.writeInbound(given);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeResult.LENGTH);
    assertThat(actual.readInt()).isEqualTo(HandshakeResult.BODY_LENGTH);
    assertThat(actual.readByte()).isEqualTo(CodecCode.MISSING_LINE_DELIMITER.getCode());
  }

  @ParameterizedTest
  @MethodSource("failedLengthRange")
  void should_return_SHORT_LENGTH_FIELD(int value) {
    // given
    ByteBuf given = HandshakeFixture.withLengthAndBody(value, ByteArrayUtils.giveMeOne(256));

    // when
    channel.writeInbound(given);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeResult.LENGTH);
    assertThat(actual.readInt()).isEqualTo(HandshakeResult.BODY_LENGTH);
    assertThat(actual.readByte()).isEqualTo(CodecCode.SHORT_LENGTH_FIELD.getCode());
  }

  @Test
  void should_return_INVALID_HAND_SHAKE_TYPE() {
    // given
    ByteBuf given = HandshakeFixture.wrongHandShakeType();

    // when
    channel.writeInbound(given);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeResult.LENGTH);
    assertThat(actual.readInt()).isEqualTo(HandshakeResult.BODY_LENGTH);
    assertThat(actual.readByte()).isEqualTo(CodecCode.INVALID_HAND_SHAKE_TYPE.getCode());
  }

  @Test
  void should_return_UNSUPPORTED_HAND_SHAKE_TYPE() {
    // given
    ByteBuf given = HandshakeFixture.wrongHandShakeType();

    // when
    channel.writeInbound(given);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeResult.LENGTH);
    assertThat(actual.readInt()).isEqualTo(HandshakeResult.BODY_LENGTH);
    assertThat(actual.readByte()).isEqualTo(CodecCode.INVALID_HAND_SHAKE_TYPE.getCode());
  }

  @Test
  void should_return_INVALID_ENCRYPTION_TYPE() {
    // given
    ByteBuf given = HandshakeFixture.wrongEncryptionType();

    // when
    channel.writeInbound(given);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeResult.LENGTH);
    assertThat(actual.readInt()).isEqualTo(HandshakeResult.BODY_LENGTH);
    assertThat(actual.readByte()).isEqualTo(CodecCode.INVALID_ENCRYPTION_TYPE.getCode());
  }

  @Test
  void should_return_INVALID_ENCRYPTION_MODE() {
    // given
    ByteBuf given = HandshakeFixture.wrongEncryptionMode();

    // when
    channel.writeInbound(given);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeResult.LENGTH);
    assertThat(actual.readInt()).isEqualTo(HandshakeResult.BODY_LENGTH);
    assertThat(actual.readByte()).isEqualTo(CodecCode.INVALID_ENCRYPTION_MODE.getCode());
  }

  @Test
  void should_return_INVALID_ENCRYPTION_PADDING() {
    // given
    ByteBuf given = HandshakeFixture.wrongEncryptionPadding();

    // when
    channel.writeInbound(given);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeResult.LENGTH);
    assertThat(actual.readInt()).isEqualTo(HandshakeResult.BODY_LENGTH);
    assertThat(actual.readByte()).isEqualTo(CodecCode.INVALID_ENCRYPTION_PADDING.getCode());
  }

  @Test
  void should_return_FAILED_DECRYPT_BODY() throws CryptoException {
    // given
    ByteBuf given = HandshakeFixture.wrongSymmetricKeySize();

    // when
    when(rsaCrypto.decrypt((byte[]) any())).thenThrow(new NullPointerException());
    channel.writeInbound(given);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeResult.LENGTH);
    assertThat(actual.readInt()).isEqualTo(HandshakeResult.BODY_LENGTH);
    assertThat(actual.readByte()).isEqualTo(CodecCode.DECRYPT_FAIL.getCode());
  }

  @Test
  void should_return_INVALID_KEY_SIZE() throws CryptoException {
    // given
    ByteBuf given = HandshakeFixture.wrongSymmetricKeySize();

    // when
    when(rsaCrypto.decrypt((byte[]) any())).thenReturn(ByteArrayUtils.giveMeOne(31));
    channel.writeInbound(given);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeResult.LENGTH);
    assertThat(actual.readInt()).isEqualTo(HandshakeResult.BODY_LENGTH);
    assertThat(actual.readByte()).isEqualTo(CodecCode.INVALID_KEY_SIZE.getCode());
  }

  @Test
  void should_return_CRYPTO_CREATE_FAIL() throws CryptoException {
    // given
    channel.pipeline().replace(HandshakeSimpleChannelInboundHandler.class, "HandshakeSimpleChannelInboundHandler",
      getCryptoThrowHandler());
    ByteBuf given = HandshakeFixture.wrongSymmetricKeySize();

    // when
    when(rsaCrypto.decrypt((byte[]) any())).thenReturn(ByteArrayUtils.giveMeOne(32));
    channel.writeInbound(given);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeResult.LENGTH);
    assertThat(actual.readInt()).isEqualTo(HandshakeResult.BODY_LENGTH);
    assertThat(actual.readByte()).isEqualTo(CodecCode.CRYPTO_CREATE_FAIL.getCode());
  }

  @Test
  void should_return_UNRECOGNIZED() throws CryptoException {
    // given
    channel.pipeline()
      .replace(CodecNames.HANDSHAKE_CHANNEL_HANDLER, "NewDecoder", getAddThrowHandler(new NullPointerException()));
    ByteBuf given = HandshakeFixture.wrongSymmetricKeySize();

    // when
    when(rsaCrypto.decrypt((byte[]) any())).thenReturn(ByteArrayUtils.giveMeOne(32));
    channel.writeInbound(given);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeResult.LENGTH);
    assertThat(actual.readInt()).isEqualTo(HandshakeResult.BODY_LENGTH);
    assertThat(actual.readByte()).isEqualTo(CodecCode.UNRECOGNIZED.getCode());
  }

  @Test
  void should_return_ENCODE_FAIL() throws CryptoException {
    // given
    channel.pipeline().replace(CodecNames.HANDSHAKE_CHANNEL_HANDLER, "NewDecoder",
      getAddThrowHandler(new Exception(new HandshakeCodecException("null", CodecCode.ENCODE_FAIL))));
    ByteBuf given = HandshakeFixture.wrongSymmetricKeySize();

    // when
    when(rsaCrypto.decrypt((byte[]) any())).thenReturn(ByteArrayUtils.giveMeOne(32));
    channel.writeInbound(given);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeResult.LENGTH);
    assertThat(actual.readInt()).isEqualTo(HandshakeResult.BODY_LENGTH);
    assertThat(actual.readByte()).isEqualTo(CodecCode.ENCODE_FAIL.getCode());
  }

  @Test
  void should_return_removed_handler() throws CryptoException {
    // given
    ByteBuf given = HandshakeFixture.wrongSymmetricKeySize();

    // when
    when(rsaCrypto.decrypt((byte[]) any())).thenReturn(ByteArrayUtils.giveMeOne(32));
    channel.writeInbound(given);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeResult.LENGTH);
    assertThat(actual.readInt()).isEqualTo(HandshakeResult.BODY_LENGTH);
    assertThat(actual.readByte()).isEqualTo(CodecCode.OK.getCode());
    assertThat(channel.pipeline().names()).doesNotContainAnyElementsOf(CodecNames.HANDSHAKES);
  }

  @Test
  void should_return_DecryptedInboundMessage() throws CryptoException {
    // given
    ByteBuf handShake = HandshakeFixture.wrongSymmetricKeySize();
    when(rsaCrypto.decrypt((byte[]) any())).thenReturn(ByteArrayUtils.giveMeOne(32));
    channel.writeInbound(handShake);
    channel.readOutbound();
    byte[] expectedBody = ByteArrayUtils.giveMeOne(128);
    InboundProtocol expectedMessage = InboundMessageFixture.create(expectedBody);
    ByteBuf given = ByteArrayFixture.create(expectedMessage);

    // when
    when(aesCrypto.decrypt((byte[]) any())).thenReturn(
      ByteArrayFixture.withoutLengthFieldAndLineDelimiter(expectedMessage).array());
    channel.writeInbound(given);
    InboundProtocol actual = channel.readInbound();

    // then
    assertThat(actual).usingRecursiveComparison().isEqualTo(expectedMessage);
  }

  @Test
  void should_return_not_found_handshake_handler() throws CryptoException {
    // given
    channel.pipeline().replace(CodecNames.HANDSHAKE_CHANNEL_HANDLER, CodecNames.HANDSHAKE_CHANNEL_HANDLER,
      new HandshakeSimpleChannelInboundHandler(rsaCrypto) {
        @Override
        public Crypto getAesCrypto(HandshakeHeader handShakeHeader, byte[] ivParameter, byte[] symmetricKey) {
          return null;
        }

        @Override
        public void addHandler(ChannelPipeline pipeline, Crypto crypto) {

        }
      });
    ByteBuf given = HandshakeFixture.wrongSymmetricKeySize();

    // when
    when(rsaCrypto.decrypt((byte[]) any())).thenReturn(ByteArrayUtils.giveMeOne(32));
    channel.writeInbound(given);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeResult.LENGTH);
    assertThat(actual.readInt()).isEqualTo(HandshakeResult.BODY_LENGTH);
    assertThat(actual.readByte()).isEqualTo(CodecCode.OK.getCode());
    assertThat(channel.pipeline().names()).doesNotContainAnyElementsOf(CodecNames.HANDSHAKES);
    assertThat(channel.pipeline().names()).hasSize(1);
  }


  private static int[] failedLengthRange() {
    return IntStream.range(
        HandshakeHeader.LENGTH_FIELD_LENGTH, HandshakeHeader.MINIMUM_LENGTH - HandshakeHeader.LENGTH_FIELD_LENGTH)
      .toArray();
  }

  private HandshakeSimpleChannelInboundHandler getHandler() {
    return new HandshakeSimpleChannelInboundHandler(rsaCrypto) {
      @Override
      public Crypto getAesCrypto(HandshakeHeader handShakeHeader, byte[] ivParameter, byte[] symmetricKey) {
        return aesCrypto;
      }

      @Override
      public void addHandler(ChannelPipeline pipeline, Crypto crypto) {
        pipeline.addLast(CodecLengthFieldBasedFrameDecoder.defaultConfiguration());
        pipeline.addLast(new ByteBufDecoder(crypto));
      }
    };
  }

  private HandshakeSimpleChannelInboundHandler getCryptoThrowHandler() {
    return new HandshakeSimpleChannelInboundHandler(rsaCrypto) {
      @Override
      public Crypto getAesCrypto(HandshakeHeader handShakeHeader, byte[] ivParameter, byte[] symmetricKey) {
        throw new NullPointerException();
      }

      @Override
      public void addHandler(ChannelPipeline pipeline, Crypto crypto) {
        pipeline.addLast(CodecLengthFieldBasedFrameDecoder.defaultConfiguration());
        pipeline.addLast(new ByteBufDecoder(crypto));
      }
    };
  }

  private HandshakeSimpleChannelInboundHandler getAddThrowHandler(Exception exception) {
    return new HandshakeSimpleChannelInboundHandler(rsaCrypto) {
      @Override
      public Crypto getAesCrypto(HandshakeHeader handShakeHeader, byte[] ivParameter, byte[] symmetricKey) {
        return aesCrypto;
      }

      @Override
      public void addHandler(ChannelPipeline pipeline, Crypto crypto) {
        try {
          throw exception;
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    };
  }
}