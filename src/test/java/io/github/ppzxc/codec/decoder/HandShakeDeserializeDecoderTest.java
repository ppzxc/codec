package io.github.ppzxc.codec.decoder;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ppzxc.codec.exception.SerializeFailedException;
import io.github.ppzxc.codec.model.DecryptedHandShakePacketFixture;
import io.github.ppzxc.codec.model.EncryptionMethod;
import io.github.ppzxc.codec.model.EncryptionMethodFixture;
import io.github.ppzxc.codec.model.HandShakePacket;
import io.github.ppzxc.codec.service.Mapper;
import io.github.ppzxc.codec.service.ObjectOutputStreamMapper;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

class HandShakeDeserializeDecoderTest {

  private Mapper mapper;
  private EmbeddedChannel channel;

  @BeforeEach
  void setUp() {
    channel = new EmbeddedChannel();
    mapper = new ObjectOutputStreamMapper();
    channel.pipeline().addLast(new HandShakeDeserializeDecoder(mapper));
  }

  @RepeatedTest(10)
  void should_deserialize() throws SerializeFailedException {
    // given
    EncryptionMethod expected = EncryptionMethodFixture.random();
    byte[] given = mapper.write(expected);

    // when
    channel.writeInbound(DecryptedHandShakePacketFixture.withBody(given));
    HandShakePacket actual = channel.readInbound();

    // then
    assertThat(actual.getEncryptionMethod()).usingRecursiveComparison().isEqualTo(expected);
  }
}