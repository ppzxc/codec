package io.github.ppzxc.codec.decoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.ppzxc.codec.Constants.LineDelimiter;
import io.github.ppzxc.codec.exception.BlankBodyCodecException;
import io.github.ppzxc.codec.exception.DecryptCodecException;
import io.github.ppzxc.codec.exception.InvalidLengthCodecException;
import io.github.ppzxc.codec.exception.MissingLineDelimiterCodecException;
import io.github.ppzxc.codec.exception.ShortLengthCodecException;
import io.github.ppzxc.codec.model.ByteArrayFixture;
import io.github.ppzxc.codec.model.Header;
import io.github.ppzxc.codec.model.InboundMessageFixture;
import io.github.ppzxc.codec.model.InboundProtocol;
import io.github.ppzxc.crypto.Crypto;
import io.github.ppzxc.crypto.CryptoException;
import io.github.ppzxc.fixh.ByteArrayUtils;
import io.github.ppzxc.fixh.ExceptionUtils;
import io.github.ppzxc.fixh.IntUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DecoderException;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ByteBufDecoderTest {

  private Crypto crypto;
  private EmbeddedChannel channel;

  @BeforeEach
  void setUp() {
    crypto = mock(Crypto.class);
    channel = new EmbeddedChannel();
    channel.pipeline().addLast(new ByteBufDecoder(crypto));
  }

  @Test
  void should_throw_BlankBodyException() {
    // given
    byte[] given = new byte[0];

    // when, then
    assertThatCode(() -> channel.writeInbound(Unpooled.wrappedBuffer(given))).satisfies(throwable -> {
      assertThat(throwable).isInstanceOf(DecoderException.class);
      assertThat(ExceptionUtils.findCause(throwable, BlankBodyCodecException.class))
        .isInstanceOf(BlankBodyCodecException.class);
    });
  }

  @ParameterizedTest
  @MethodSource("allMessageLength")
  void should_throw_ShortLengthException(int minimumMessageLength) {
    // given
    byte[] given = ByteArrayUtils.giveMeOne(minimumMessageLength);

    // when, then
    assertThatCode(() -> channel.writeInbound(Unpooled.wrappedBuffer(given))).satisfies(throwable -> {
      assertThat(throwable).isInstanceOf(DecoderException.class);
      assertThat(ExceptionUtils.findCause(throwable, ShortLengthCodecException.class))
        .isInstanceOf(ShortLengthCodecException.class);
    });
  }

  @Test
  void should_throw_MissingLineDelimiterException() {
    // given
    ByteBuf given = Unpooled.wrappedBuffer(ByteArrayFixture.withoutLineDelimiter());

    // when, then
    assertThatCode(() -> channel.writeInbound(given)).satisfies(throwable -> {
      assertThat(throwable).isInstanceOf(DecoderException.class);
      assertThat(ExceptionUtils.findCause(throwable, MissingLineDelimiterCodecException.class))
        .isInstanceOf(MissingLineDelimiterCodecException.class);
    });
  }

  @Test
  void should_throw_InvalidLengthException() {
    // given
    ByteBuf given = ByteArrayFixture.withLineDelimiter(IntUtils.giveMeOne(128, 256), ByteArrayUtils.giveMeOne(512));

    // when, then
    assertThatCode(() -> channel.writeInbound(given)).satisfies(
      throwable -> {
        assertThat(throwable).isInstanceOf(DecoderException.class);
        assertThat(ExceptionUtils.findCause(throwable, InvalidLengthCodecException.class))
          .isInstanceOf(InvalidLengthCodecException.class);
      });
  }

  @Test
  void should_throw_DecryptFailException() throws CryptoException {
    // given
    byte[] expectedBody = ByteArrayUtils.giveMeOne(128);
    InboundProtocol expectedMessage = InboundMessageFixture.create(expectedBody);

    // when
    when(crypto.decrypt((byte[]) any())).thenThrow(new NullPointerException());

    // then
    assertThatCode(() -> channel.writeInbound(InboundMessageFixture.to(expectedMessage))).satisfies(throwable -> {
      assertThat(throwable).isInstanceOf(DecoderException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(NullPointerException.class);
      assertThat(ExceptionUtils.findCause(throwable, DecryptCodecException.class))
        .isInstanceOf(DecryptCodecException.class);
    });
  }

  @Test
  void should_return_InboundMessage() throws CryptoException {
    // given
    byte[] expectedBody = ByteArrayUtils.giveMeOne(128);
    InboundProtocol expectedMessage = InboundMessageFixture.create(expectedBody);
    ByteBuf given = ByteArrayFixture.create(expectedMessage);

    // when
    when(crypto.decrypt((byte[]) any())).thenReturn(ByteArrayFixture.withoutLengthFieldAndLineDelimiter(expectedMessage).array());
    channel.writeInbound(given);
    InboundProtocol actual = channel.readInbound();

    // then
    assertThat(actual).usingRecursiveComparison().isEqualTo(expectedMessage);
  }

  private static int[] allMessageLength() {
    return IntStream.range(1, Header.ID_FIELD_LENGTH + LineDelimiter.LENGTH).toArray();
  }
}