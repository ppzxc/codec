package io.github.ppzxc.codec.decoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.github.ppzxc.codec.ByteArrayFixtureFactory;
import io.github.ppzxc.codec.decoder.FixedConstructorLengthFieldBasedFrameDecoder;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FixedConstructorLengthFieldBasedFrameDecoderTest {

  private EmbeddedChannel channel;

  @BeforeEach
  void setUp() {
    channel = new EmbeddedChannel();
    channel.pipeline().addLast(FixedConstructorLengthFieldBasedFrameDecoder.defaultConfiguration());
  }

  @Test
  void should_return_given_byte_array() {
    // given: random header with empty body.
    byte[] given = ByteArrayFixtureFactory.randomWithEmptyBody();

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
      Unpooled.copiedBuffer(ByteArrayFixtureFactory.randomWithBody(IntUtils.giveMeNegative()))))
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
    assertThat(channel.inboundMessages()).hasSize(0);
  }

  @RepeatedTest(10)
  void should_success_received_when_inbound_relay_empty_body_packet() {
    // given: three empty packet
    int packetCount = IntUtils.giveMeOne(1, 100);
    ByteBuf given = Unpooled.buffer(12 * packetCount);
    List<byte[]> givenPacketList = new ArrayList<>();
    for (int i = 0; i < packetCount; i++) {
      byte[] givenPacket = ByteArrayFixtureFactory.randomWithEmptyBody();
      givenPacketList.add(givenPacket);
      given.writeBytes(givenPacket);
    }

    // when
    channel.writeInbound(given);

    // then
    assertThat(channel.inboundMessages()).hasSize(packetCount);
    for (byte[] actual : givenPacketList) {
      byte[] read = new byte[12];
      channel.<ByteBuf>readInbound().readBytes(read);
      assertThat(read).isEqualTo(actual);
    }
  }
}