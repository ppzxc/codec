package io.github.ppzxc.codec.decoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.ppzxc.codec.exception.BlankBodyException;
import io.github.ppzxc.codec.exception.DecryptFailException;
import io.github.ppzxc.codec.exception.InvalidLengthException;
import io.github.ppzxc.codec.exception.MissingLineDelimiterException;
import io.github.ppzxc.codec.exception.ShortLengthException;
import io.github.ppzxc.codec.model.ByteArrayFixture;
import io.github.ppzxc.codec.model.Header;
import io.github.ppzxc.codec.model.InboundMessage;
import io.github.ppzxc.codec.model.InboundMessageFixture;
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

class EncryptedInboundMessageDecoderTest {

  private Crypto crypto;
  private EmbeddedChannel channel;

  @BeforeEach
  void setUp() {
    crypto = mock(Crypto.class);
    channel = new EmbeddedChannel();
    channel.pipeline().addLast(new EncryptedInboundMessageDecoder(crypto));
  }

  @Test
  void should_throw_BlankBodyException() {
    // given
    byte[] given = new byte[0];

    // when, then
    assertThatCode(() -> channel.writeInbound(Unpooled.wrappedBuffer(given))).satisfies(throwable -> {
      assertThat(throwable).isInstanceOf(DecoderException.class);
      assertThat(ExceptionUtils.findCause(throwable, BlankBodyException.class))
        .isInstanceOf(BlankBodyException.class);
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
      assertThat(ExceptionUtils.findCause(throwable, ShortLengthException.class))
        .isInstanceOf(ShortLengthException.class);
    });
  }

  @Test
  void should_throw_MissingLineDelimiterException() {
    // given
    ByteBuf given = Unpooled.wrappedBuffer(ByteArrayFixture.withoutLineDelimiter());

    // when, then
    assertThatCode(() -> channel.writeInbound(given)).satisfies(throwable -> {
      assertThat(throwable).isInstanceOf(DecoderException.class);
      assertThat(ExceptionUtils.findCause(throwable, MissingLineDelimiterException.class))
        .isInstanceOf(MissingLineDelimiterException.class);
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
        assertThat(ExceptionUtils.findCause(throwable, InvalidLengthException.class))
          .isInstanceOf(InvalidLengthException.class);
      });
  }

  @Test
  void should_throw_DecryptFailException() throws CryptoException {
    // given
    byte[] expectedBody = ByteArrayUtils.giveMeOne(128);
    InboundMessage expectedMessage = InboundMessageFixture.create(expectedBody);

    // when
    when(crypto.decrypt((byte[]) any())).thenThrow(new NullPointerException());

    // then
    assertThatCode(() -> channel.writeInbound(InboundMessageFixture.to(expectedMessage))).satisfies(throwable -> {
      assertThat(throwable).isInstanceOf(DecoderException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(NullPointerException.class);
      assertThat(ExceptionUtils.findCause(throwable, DecryptFailException.class))
        .isInstanceOf(DecryptFailException.class);
    });
  }

  @Test
  void should_return_InboundMessage() throws CryptoException {
    // given
    byte[] expectedBody = ByteArrayUtils.giveMeOne(128);
    InboundMessage expectedMessage = InboundMessageFixture.create(expectedBody);
    ByteBuf given = ByteArrayFixture.create(expectedMessage);

    // when
    when(crypto.decrypt((byte[]) any())).thenReturn(ByteArrayFixture.withoutLengthFieldAndLineDelimiter(expectedMessage).array());
    channel.writeInbound(given);
    InboundMessage actual = channel.readInbound();

    // then
    assertThat(actual).usingRecursiveComparison().isEqualTo(expectedMessage);
  }

  private static int[] allMessageLength() {
    return IntStream.range(1, Header.ID_FIELD_LENGTH + Header.LINE_DELIMITER_LENGTH).toArray();
  }
}