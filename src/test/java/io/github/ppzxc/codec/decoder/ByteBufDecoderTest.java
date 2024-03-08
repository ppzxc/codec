package io.github.ppzxc.codec.decoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.github.ppzxc.codec.exception.LessThanMinimumPacketLengthCodeException;
import io.github.ppzxc.codec.exception.MissingLineDelimiterCodeException;
import io.github.ppzxc.codec.exception.NotSameLengthCodeException;
import io.github.ppzxc.codec.exception.NotSupportedBodyLengthException;
import io.github.ppzxc.codec.exception.NullPointerCodeException;
import io.github.ppzxc.codec.exception.ProblemCodeException;
import io.github.ppzxc.codec.model.Header;
import io.github.ppzxc.codec.model.HeaderFixture;
import io.github.ppzxc.codec.model.RawInboundPacket;
import io.github.ppzxc.codec.model.RawInboundPacketFixture;
import io.github.ppzxc.fixh.ByteArrayUtils;
import io.github.ppzxc.fixh.ExceptionUtils;
import io.github.ppzxc.fixh.IntUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.CodecException;
import io.netty.handler.codec.DecoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
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
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(ProblemCodeException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(NullPointerCodeException.class);
    });
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13})
  void should_throws_exception_when_length_less_then_minimum_packet_length(int length) {
    // given
    ByteBuf given = Unpooled.copiedBuffer(ByteArrayUtils.giveMeOne(length));

    // when
    assertThatCode(() -> channel.writeInbound(given)).satisfies(throwable -> {
      assertThat(throwable).isInstanceOf(DecoderException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(ProblemCodeException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(LessThanMinimumPacketLengthCodeException.class);
    });
  }

  @RepeatedTest(10)
  void should_return_raw_packet_when_empty_body_packet() {
    // given
    RawInboundPacket given = RawInboundPacketFixture.emptyBodyWithLineDelimiter();

    // when
    channel.writeInbound(RawInboundPacketFixture.toByteBuf(given));
    RawInboundPacket actual = channel.readInbound();

    // then
    assertThat(actual.getHeader()).usingRecursiveComparison().isEqualTo(given.getHeader());
    assertThat(ByteBufUtil.equals(actual.getBody(), given.getBody())).isTrue();
  }

  @RepeatedTest(10)
  void should_return_raw_packet_when_use_body_packet() {
    // given
    RawInboundPacket given = RawInboundPacketFixture.withBody(IntUtils.giveMePositive(1024 * 1024));

    // when
    channel.writeInbound(RawInboundPacketFixture.toByteBuf(given));
    RawInboundPacket actual = channel.readInbound();

    // then
    assertThat(actual.getHeader()).usingRecursiveComparison().isEqualTo(given.getHeader());
    byte[] actualBody = new byte[actual.getBody().readableBytes()];
    actual.getBody().readBytes(actualBody);
    assertThat(actualBody).isEqualTo(given.getBody().array());
  }

  @RepeatedTest(10)
  void should_throw_not_supported_body_length_when_overflow_body() {
    // given
    RawInboundPacket given = RawInboundPacketFixture.withBody(IntUtils.giveMeOne(1024 * 1024 * 4, 1024 * 1024 * 8));

    // when
    assertThatCode(() -> channel.writeInbound(RawInboundPacketFixture.toByteBuf(given))).satisfies(throwable -> {
      assertThat(throwable).isInstanceOf(CodecException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(ProblemCodeException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(NotSupportedBodyLengthException.class);
    });
  }

  @RepeatedTest(10)
  void should_throw_exception_when_normal_header_but_overflow_body() {
    // given
    int bodyLength = IntUtils.giveMeOne(1024 * 1024 * 4, 1024 * 1024 * 8);
    ByteBuf body = Unpooled.buffer(bodyLength + Header.MINIMUM_BODY_LENGTH);
    body.writeBytes(ByteArrayUtils.giveMeOne(bodyLength));
    body.writeBytes(RawInboundPacket.LINE_DELIMITER);
    RawInboundPacket given = RawInboundPacketFixture.create(HeaderFixture.random(1024 * 1024), body);

    // when
    assertThatCode(() -> channel.writeInbound(RawInboundPacketFixture.toByteBuf(given))).satisfies(throwable -> {
      assertThat(throwable).isInstanceOf(CodecException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(ProblemCodeException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(NotSupportedBodyLengthException.class);
    });
  }

  @RepeatedTest(10)
  void should_throw_exception_when_not_equals_header_body_length_real_body_length() {
    // given
    int bodyLength = IntUtils.giveMeOne(1024 * 1024 * 2);
    ByteBuf body = Unpooled.buffer(bodyLength + Header.MINIMUM_BODY_LENGTH);
    body.writeBytes(ByteArrayUtils.giveMeOne(bodyLength));
    body.writeBytes(RawInboundPacket.LINE_DELIMITER);
    RawInboundPacket given = RawInboundPacketFixture.create(HeaderFixture.random(1024 * 1024), body);

    // when
    assertThatCode(() -> channel.writeInbound(RawInboundPacketFixture.toByteBuf(given))).satisfies(throwable -> {
      assertThat(throwable).isInstanceOf(CodecException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(ProblemCodeException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(NotSameLengthCodeException.class);
    });
  }

  @RepeatedTest(10)
  void should_throw_exception_when_not_contains_line_delimiter() {
    // given
    RawInboundPacket given = RawInboundPacketFixture.withBodyWithoutLineDelimiter(IntUtils.giveMeOne(1024 * 1024 * 2));

    // when
    assertThatCode(() -> channel.writeInbound(RawInboundPacketFixture.toByteBuf(given))).satisfies(throwable -> {
      assertThat(throwable).isInstanceOf(CodecException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(ProblemCodeException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(MissingLineDelimiterCodeException.class);
    });
  }
}