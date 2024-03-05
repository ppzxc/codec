package io.github.ppzxc.codec.encoder;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ppzxc.codec.model.RawOutboundPacket;
import io.github.ppzxc.codec.model.RawOutboundPacketFixture;
import io.github.ppzxc.fixh.IntUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

class RawOutboundPacketEncoderTest {

  private EmbeddedChannel channel;

  @BeforeEach
  void setUp() {
    channel = new EmbeddedChannel();
    channel.pipeline().addLast(new RawOutboundPacketEncoder());
  }

  @RepeatedTest(10)
  void should_serialized_when_with_empty_body() {
    // given
    RawOutboundPacket expected = RawOutboundPacketFixture.emptyBodyWithLineDelimiter();

    // when
    channel.writeOutbound(expected);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readInt()).isEqualTo(expected.getHeader().getId());
    assertThat(actual.readByte()).isEqualTo(expected.getHeader().getType());
    assertThat(actual.readByte()).isEqualTo(expected.getHeader().getStatus());
    assertThat(actual.readByte()).isEqualTo(expected.getHeader().getEncoding());
    assertThat(actual.readByte()).isEqualTo(expected.getHeader().getReserved());
    assertThat(actual.readInt()).isEqualTo(expected.getHeader().getBodyLength());
    byte[] actualBody = new byte[actual.readableBytes()];
    actual.readBytes(actualBody);
    assertThat(actualBody).isEqualTo(expected.getBody().array());
  }

  @RepeatedTest(10)
  void should_serialized_when_with_body() {
    // given
    RawOutboundPacket expected = RawOutboundPacketFixture.withBody(IntUtils.giveMeOne(1, 1024 * 1024));

    // when
    channel.writeOutbound(expected);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readInt()).isEqualTo(expected.getHeader().getId());
    assertThat(actual.readByte()).isEqualTo(expected.getHeader().getType());
    assertThat(actual.readByte()).isEqualTo(expected.getHeader().getStatus());
    assertThat(actual.readByte()).isEqualTo(expected.getHeader().getEncoding());
    assertThat(actual.readByte()).isEqualTo(expected.getHeader().getReserved());
    assertThat(actual.readInt()).isEqualTo(expected.getHeader().getBodyLength());
    byte[] actualBody = new byte[actual.readableBytes()];
    actual.readBytes(actualBody);
    assertThat(actualBody).isEqualTo(expected.getBody().array());
  }
}