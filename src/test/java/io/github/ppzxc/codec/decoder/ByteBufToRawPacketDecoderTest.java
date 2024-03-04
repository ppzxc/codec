package io.github.ppzxc.codec.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ByteBufToRawPacketDecoderTest {

  private EmbeddedChannel channel;

  @BeforeEach
  void setUp() {
    channel = new EmbeddedChannel();
    channel.pipeline().addLast(new ByteBufToRawPacketDecoder());
  }

  @Test
  void should_throws_exception_when_null_byte_buf() {
    // given
    ByteBuf given = null;

    // when
    channel.writeInbound(given);
  }

  @Test
  void should_throws_exception_when_empty_byte_buf() {
    // given
    ByteBuf given = Unpooled.buffer(0);

    // when
    channel.writeInbound(given);
  }
}