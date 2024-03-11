package io.github.ppzxc.codec.decoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.github.ppzxc.codec.model.ByteArrayFixture;
import io.github.ppzxc.fixh.ByteArrayUtils;
import io.github.ppzxc.fixh.IntUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FixedConstructorLengthFieldBasedFrameDecoderTest {

  private EmbeddedChannel channel;

  @BeforeEach
  void setUp() {
    channel = new EmbeddedChannel();
    channel.pipeline().addLast(FixedConstructorLengthFieldBasedFrameDecoder.defaultConfiguration());
  }

  @RepeatedTest(10)
  void should_return_given_byte_array() {
    // given: random header with empty body.
    byte[] given = ByteArrayFixture.randomWithEmptyBody();

    // when
    channel.writeInbound(Unpooled.copiedBuffer(given));
    ByteBuf actual = channel.readInbound();

    // then
    assertThat(actual).isNotNull();
    assertThat(actual.array()).isEqualTo(given);
  }

  @RepeatedTest(10)
  void should_throw_when_negative_body_length() {
    // given, when, then
    assertThatCode(() -> channel.writeInbound(
      Unpooled.copiedBuffer(ByteArrayFixture.randomWithBody(IntUtils.giveMeNegative()))))
      .isInstanceOfAny(TooLongFrameException.class);
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
  void should_wait_when_header_size_is_less_then_default_size(int headerLength) {
    // given
    byte[] given = ByteArrayUtils.giveMeOne(headerLength);

    // when
    boolean actual = channel.writeInbound(Unpooled.copiedBuffer(given));

    // then
    assertThat(actual).isFalse();
    assertThat(channel.inboundMessages()).isEmpty();
  }

  @RepeatedTest(10)
  void should_success_received_when_inbound_relay_empty_body_message() {
    // given: three empty message
    int messageCount = IntUtils.giveMeOne(1, 100);
    ByteBuf given = Unpooled.buffer(12 * messageCount);
    List<byte[]> givenList = new ArrayList<>();
    for (int i = 0; i < messageCount; i++) {
      byte[] givenMessage = ByteArrayFixture.randomWithEmptyBody();
      givenList.add(givenMessage);
      given.writeBytes(givenMessage);
    }

    // when
    channel.writeInbound(given);

    // then
    assertThat(channel.inboundMessages()).hasSize(messageCount);
    for (byte[] actual : givenList) {
      byte[] read = new byte[12];
      channel.<ByteBuf>readInbound().readBytes(read);
      assertThat(read).isEqualTo(actual);
    }
  }
}