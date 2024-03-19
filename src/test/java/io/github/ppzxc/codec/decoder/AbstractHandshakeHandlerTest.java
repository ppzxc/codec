package io.github.ppzxc.codec.decoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.ppzxc.codec.model.ByteArrayFixture;
import io.github.ppzxc.codec.model.CodecProblemCode;
import io.github.ppzxc.codec.model.HandshakeFixture;
import io.github.ppzxc.codec.model.HandshakeHeader;
import io.github.ppzxc.codec.model.InboundMessage;
import io.github.ppzxc.codec.model.InboundMessageFixture;
import io.github.ppzxc.crypto.Crypto;
import io.github.ppzxc.crypto.CryptoException;
import io.github.ppzxc.fixh.ByteArrayUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class AbstractHandshakeHandlerTest {

  private Crypto rsaCrypto;
  private Crypto aesCrypto;
  private EmbeddedChannel channel;

  @BeforeEach
  void setUp() {
    rsaCrypto = mock(Crypto.class);
    aesCrypto = mock(Crypto.class);
    channel = new EmbeddedChannel();
    channel.pipeline().addLast("Decoder", new AbstractHandshakeHandler(rsaCrypto) {
      @Override
      public Crypto getAesCrypto(HandshakeHeader handShakeHeader, byte[] ivParameter, byte[] symmetricKey)
        throws Exception {
        return aesCrypto;
      }

      @Override
      public void addHandler(ChannelPipeline pipeline, Crypto crypto) {
        pipeline.addLast(new EncryptedInboundMessageDecoder(crypto));
      }
    });
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3})
  void should_return_failed_code_when_short_buffer(int value) {
    // given
    byte[] given = ByteArrayUtils.giveMeOne(value);

    // when
    channel.writeInbound(Unpooled.wrappedBuffer(given));
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeHeader.RESULT_LENGTH);
    assertThat(actual.readInt()).isEqualTo(1);
    assertThat(actual.readByte()).isEqualTo(CodecProblemCode.SHORT_LENGTH.getCode());
  }

  @ParameterizedTest
  @MethodSource("failedLengthRange")
  void should_return_failed_code_when_short_length(int value) {
    // given
    byte[] given = ByteArrayFixture.create(value,
      ByteArrayUtils.giveMeOne(HandshakeHeader.MINIMUM_LENGTH - HandshakeHeader.LENGTH_FIELD_LENGTH));

    // when
    channel.writeInbound(Unpooled.wrappedBuffer(given));
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeHeader.RESULT_LENGTH);
    assertThat(actual.readInt()).isEqualTo(1);
    assertThat(actual.readByte()).isEqualTo(CodecProblemCode.SHORT_LENGTH_FIELD.getCode());
  }

  @Test
  void should_return_INVALID_HAND_SHAKE_TYPE() {
    // given
    ByteBuf given = HandshakeFixture.wrongHandShakeType();

    // when
    channel.writeInbound(Unpooled.wrappedBuffer(given));
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeHeader.RESULT_LENGTH);
    assertThat(actual.readInt()).isEqualTo(1);
    assertThat(actual.readByte()).isEqualTo(CodecProblemCode.INVALID_HAND_SHAKE_TYPE.getCode());
  }

  @Test
  void should_return_UNSUPPORTED_HAND_SHAKE_TYPE() {
    // given
    ByteBuf given = HandshakeFixture.wrongHandShakeType();

    // when
    channel.writeInbound(Unpooled.wrappedBuffer(given));
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeHeader.RESULT_LENGTH);
    assertThat(actual.readInt()).isEqualTo(1);
    assertThat(actual.readByte()).isEqualTo(CodecProblemCode.INVALID_HAND_SHAKE_TYPE.getCode());
  }

  @Test
  void should_return_INVALID_ENCRYPTION_TYPE() {
    // given
    ByteBuf given = HandshakeFixture.wrongEncryptionType();

    // when
    channel.writeInbound(Unpooled.wrappedBuffer(given));
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeHeader.RESULT_LENGTH);
    assertThat(actual.readInt()).isEqualTo(1);
    assertThat(actual.readByte()).isEqualTo(CodecProblemCode.INVALID_ENCRYPTION_TYPE.getCode());
  }

  @Test
  void should_return_INVALID_ENCRYPTION_MODE() {
    // given
    ByteBuf given = HandshakeFixture.wrongEncryptionMode();

    // when
    channel.writeInbound(Unpooled.wrappedBuffer(given));
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeHeader.RESULT_LENGTH);
    assertThat(actual.readInt()).isEqualTo(1);
    assertThat(actual.readByte()).isEqualTo(CodecProblemCode.INVALID_ENCRYPTION_MODE.getCode());
  }

  @Test
  void should_return_INVALID_ENCRYPTION_PADDING() {
    // given
    ByteBuf given = HandshakeFixture.wrongEncryptionPadding();

    // when
    channel.writeInbound(Unpooled.wrappedBuffer(given));
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeHeader.RESULT_LENGTH);
    assertThat(actual.readInt()).isEqualTo(1);
    assertThat(actual.readByte()).isEqualTo(CodecProblemCode.INVALID_ENCRYPTION_PADDING.getCode());
  }

  @Test
  void should_return_FAILED_DECRYPT_BODY() throws CryptoException {
    // given
    ByteBuf given = HandshakeFixture.wrongSymmetricKeySize();

    // when
    when(rsaCrypto.decrypt((byte[]) any())).thenThrow(new NullPointerException());
    channel.writeInbound(Unpooled.wrappedBuffer(given));
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeHeader.RESULT_LENGTH);
    assertThat(actual.readInt()).isEqualTo(1);
    assertThat(actual.readByte()).isEqualTo(CodecProblemCode.DECRYPT_FAIL.getCode());
  }

  @Test
  void should_return_INVALID_KEY_SIZE() throws CryptoException {
    // given
    ByteBuf given = HandshakeFixture.wrongSymmetricKeySize();

    // when
    when(rsaCrypto.decrypt((byte[]) any())).thenReturn(ByteArrayUtils.giveMeOne(31));
    channel.writeInbound(Unpooled.wrappedBuffer(given));
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeHeader.RESULT_LENGTH);
    assertThat(actual.readInt()).isEqualTo(1);
    assertThat(actual.readByte()).isEqualTo(CodecProblemCode.INVALID_KEY_SIZE.getCode());
  }

  @Test
  void should_return_removed_handler() throws CryptoException {
    // given
    ByteBuf given = HandshakeFixture.wrongSymmetricKeySize();

    // when
    when(rsaCrypto.decrypt((byte[]) any())).thenReturn(ByteArrayUtils.giveMeOne(32));
    channel.writeInbound(Unpooled.wrappedBuffer(given));
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeHeader.RESULT_LENGTH);
    assertThat(actual.readInt()).isEqualTo(1);
    assertThat(actual.readByte()).isEqualTo(CodecProblemCode.OK.getCode());
    assertThat(channel.pipeline().names()).doesNotContain("HandShakeHandler");
  }

  @Test
  void should_return_DecryptedInboundMessage() throws CryptoException {
    // given
    ByteBuf handShake = HandshakeFixture.wrongSymmetricKeySize();
    when(rsaCrypto.decrypt((byte[]) any())).thenReturn(ByteArrayUtils.giveMeOne(32));
    channel.writeInbound(Unpooled.wrappedBuffer(handShake));
    channel.readOutbound();
    byte[] expectedBody = ByteArrayUtils.giveMeOne(128);
    InboundMessage expectedMessage = InboundMessageFixture.create(expectedBody);
    ByteBuf given = ByteArrayFixture.create(expectedMessage);

    // when
    when(aesCrypto.decrypt((byte[]) any())).thenReturn(
      ByteArrayFixture.withoutLengthFieldAndLineDelimiter(expectedMessage).array());
    channel.writeInbound(given);
    InboundMessage actual = channel.readInbound();

    // then
    assertThat(actual).usingRecursiveComparison().isEqualTo(expectedMessage);
  }

  private static int[] failedLengthRange() {
    return IntStream.range(
        HandshakeHeader.LENGTH_FIELD_LENGTH, HandshakeHeader.MINIMUM_LENGTH - HandshakeHeader.LENGTH_FIELD_LENGTH)
      .toArray();
  }
}