package io.github.ppzxc.codec.decoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.github.ppzxc.codec.exception.LessThanMinimumMessageLengthCodeProblemException;
import io.github.ppzxc.codec.exception.MissingLineDelimiterCodeProblemException;
import io.github.ppzxc.codec.exception.NotSameLengthCodeProblemException;
import io.github.ppzxc.codec.exception.NotSupportedBodyLengthProblemException;
import io.github.ppzxc.codec.exception.NullPointerCodeProblemException;
import io.github.ppzxc.codec.exception.CodecProblemException;
import io.github.ppzxc.codec.model.AbstractMessage;
import io.github.ppzxc.codec.model.EncryptedHandShakeMessage;
import io.github.ppzxc.codec.model.Header;
import io.github.ppzxc.codec.model.HeaderFixture;
import io.github.ppzxc.codec.model.InboundMessage;
import io.github.ppzxc.codec.model.RawMessageFixture;
import io.github.ppzxc.fixh.ByteArrayUtils;
import io.github.ppzxc.fixh.ExceptionUtils;
import io.github.ppzxc.fixh.IntUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DecoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ByteBufDecoderTest {

  private EmbeddedChannel channel;

  @BeforeEach
  void setUp() {
    channel = new EmbeddedChannel();
    channel.pipeline().addLast(new ByteBufDecoder());
  }

  @RepeatedTest(10)
  void should_throws_exception_when_null_byte_buf() {
    // given
    ByteBuf given = null;

    // when
    assertThatCode(() -> channel.writeInbound(given)).isInstanceOf(NullPointerException.class);
  }

  @RepeatedTest(10)
  void should_throws_exception_when_empty_byte_buf() {
    // given
    ByteBuf given = Unpooled.buffer(0);

    // when
    assertThatCode(() -> channel.writeInbound(given)).satisfies(throwable -> {
      assertThat(throwable).isInstanceOf(DecoderException.class);
      assertThat(throwable).isInstanceOf(Object.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(CodecProblemException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(NullPointerCodeProblemException.class);
    });
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13})
  void should_throws_exception_when_length_less_then_minimum_message_length(int length) {
    // given
    ByteBuf given = Unpooled.copiedBuffer(ByteArrayUtils.giveMeOne(length));

    // when
    assertThatCode(() -> channel.writeInbound(given)).satisfies(throwable -> {
      assertThat(throwable).isInstanceOf(DecoderException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(CodecProblemException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(LessThanMinimumMessageLengthCodeProblemException.class);
    });
  }

  @RepeatedTest(10)
  void should_return_raw_message_when_empty_body_message() {
    // given
    InboundMessage given = RawMessageFixture.emptyBodyWithLineDelimiter();

    // when
    channel.writeInbound(RawMessageFixture.toByteBuf(given));
    InboundMessage actual = channel.readInbound();

    // then
    assertThat(actual.header()).usingRecursiveComparison().isEqualTo(given.header());
    assertThat(ByteBufUtil.equals(actual.getBody(), given.getBody())).isTrue();
  }

  @RepeatedTest(10)
  void should_return_raw_message_when_use_body_message() {
    // given
    InboundMessage given = RawMessageFixture.withBody(IntUtils.giveMePositive(1024 * 1024));

    // when
    channel.writeInbound(RawMessageFixture.toByteBuf(given));
    InboundMessage actual = channel.readInbound();

    // then
    assertThat(actual.header()).usingRecursiveComparison().isEqualTo(given.header());
    byte[] actualBody = new byte[actual.getBody().readableBytes()];
    actual.getBody().readBytes(actualBody);
    assertThat(actualBody).isEqualTo(given.getBody().array());
  }

  @RepeatedTest(10)
  void should_throw_not_supported_body_length_when_overflow_body() {
    // given
    InboundMessage given = RawMessageFixture.withBody(IntUtils.giveMeOne(1024 * 1024 * 4, 1024 * 1024 * 8));

    // when
    assertThatCode(() -> channel.writeInbound(RawMessageFixture.toByteBuf(given))).satisfies(throwable -> {
      assertThat(throwable).isInstanceOf(io.netty.handler.codec.CodecException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(CodecProblemException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(NotSupportedBodyLengthProblemException.class);
    });
  }

  @RepeatedTest(10)
  void should_throw_exception_when_normal_header_but_overflow_body() {
    // given
    int bodyLength = IntUtils.giveMeOne(1024 * 1024 * 4, 1024 * 1024 * 8);
    ByteBuf body = Unpooled.buffer(bodyLength + Header.MINIMUM_BODY_LENGTH);
    body.writeBytes(ByteArrayUtils.giveMeOne(bodyLength));
    body.writeBytes(AbstractMessage.LINE_DELIMITER);
    InboundMessage given = RawMessageFixture.create(HeaderFixture.random(1024 * 1024), body);

    // when
    assertThatCode(() -> channel.writeInbound(RawMessageFixture.toByteBuf(given))).satisfies(throwable -> {
      assertThat(throwable).isInstanceOf(io.netty.handler.codec.CodecException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(CodecProblemException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(NotSupportedBodyLengthProblemException.class);
    });
  }

  @RepeatedTest(10)
  void should_throw_exception_when_not_equals_header_body_length_real_body_length() {
    // given
    int bodyLength = IntUtils.giveMeOne(1024 * 1024 * 2);
    ByteBuf body = Unpooled.buffer(bodyLength + Header.MINIMUM_BODY_LENGTH);
    body.writeBytes(ByteArrayUtils.giveMeOne(bodyLength));
    body.writeBytes(AbstractMessage.LINE_DELIMITER);
    InboundMessage given = RawMessageFixture.create(HeaderFixture.random(1024 * 1024), body);

    // when
    assertThatCode(() -> channel.writeInbound(RawMessageFixture.toByteBuf(given))).satisfies(throwable -> {
      assertThat(throwable).isInstanceOf(io.netty.handler.codec.CodecException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(CodecProblemException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(NotSameLengthCodeProblemException.class);
    });
  }

  @RepeatedTest(10)
  void should_throw_exception_when_not_contains_line_delimiter() {
    // given
    InboundMessage given = RawMessageFixture.withBodyWithoutLineDelimiter(IntUtils.giveMeOne(1024 * 1024 * 2));

    // when
    assertThatCode(() -> channel.writeInbound(RawMessageFixture.toByteBuf(given))).satisfies(throwable -> {
      assertThat(throwable).isInstanceOf(io.netty.handler.codec.CodecException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(CodecProblemException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(MissingLineDelimiterCodeProblemException.class);
    });
  }

  @Test
  void should_return_EncryptedHandShakeMessage() {
    // given
    InboundMessage given = RawMessageFixture.withFakeHandShake();

    // when
    channel.writeInbound(RawMessageFixture.toByteBuf(given));
    EncryptedHandShakeMessage actual = channel.readInbound();

    // then
    assertThat(actual.header()).usingRecursiveComparison().isEqualTo(given.header());
    assertThat(ByteBufUtil.equals(actual.getBody(), given.getBody())).isTrue();
  }
}