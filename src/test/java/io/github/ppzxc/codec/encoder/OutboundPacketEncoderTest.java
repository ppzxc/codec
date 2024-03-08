package io.github.ppzxc.codec.encoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.github.ppzxc.codec.exception.DeserializeFailedException;
import io.github.ppzxc.codec.exception.OutboundPacketEncodeFailException;
import io.github.ppzxc.codec.exception.ProblemCodeException;
import io.github.ppzxc.codec.mapper.DefaultMultiMapper;
import io.github.ppzxc.codec.mapper.MultiMapper;
import io.github.ppzxc.codec.mapper.ReadCommand;
import io.github.ppzxc.codec.model.EncodingType;
import io.github.ppzxc.codec.model.HeaderFixture;
import io.github.ppzxc.codec.model.PrepareOutboundPacket;
import io.github.ppzxc.codec.model.PrepareOutboundPacketFixture;
import io.github.ppzxc.codec.model.TestUser;
import io.github.ppzxc.crypto.Crypto;
import io.github.ppzxc.crypto.CryptoException;
import io.github.ppzxc.crypto.CryptoFactory;
import io.github.ppzxc.fixh.ExceptionUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.EncoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

class OutboundPacketEncoderTest {

  private Crypto crypto;
  private MultiMapper multiMapper;
  private EmbeddedChannel channel;

  @BeforeEach
  void setUp() {
    crypto = CryptoFactory.aes128();
    multiMapper = DefaultMultiMapper.create();
    channel = new EmbeddedChannel();
    channel.pipeline().addLast(new OutboundPacketEncoder(crypto, multiMapper));
  }

  @RepeatedTest(10)
  void should_encode_prepare_outbound_packet() throws CryptoException, DeserializeFailedException {
    // given
    TestUser given = TestUser.random();
    PrepareOutboundPacket expected = PrepareOutboundPacketFixture.create(HeaderFixture.with(EncodingType.JSON), given);

    // when
    channel.writeOutbound(expected);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readInt()).isEqualTo(expected.getHeader().getId());
    assertThat(actual.readByte()).isEqualTo(expected.getHeader().getType());
    assertThat(actual.readByte()).isEqualTo(expected.getHeader().getStatus());
    assertThat(actual.readByte()).isEqualTo(expected.getHeader().getEncoding());
    assertThat(actual.readByte()).isEqualTo(expected.getHeader().getReserved());
    assertThat(actual.readInt()).isGreaterThan(2);
    equalsBody(getBody(actual), given);
  }

  @RepeatedTest(10)
  void should_encode_prepare_outbound_packet_when_null_body() {
    // given
    PrepareOutboundPacket expected = PrepareOutboundPacketFixture.create(HeaderFixture.random(), null);

    // when
    channel.writeOutbound(expected);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readInt()).isEqualTo(expected.getHeader().getId());
    assertThat(actual.readByte()).isEqualTo(expected.getHeader().getType());
    assertThat(actual.readByte()).isEqualTo(expected.getHeader().getStatus());
    assertThat(actual.readByte()).isEqualTo(expected.getHeader().getEncoding());
    assertThat(actual.readByte()).isEqualTo(expected.getHeader().getReserved());
    assertThat(actual.readInt()).isEqualTo(2);
    checkLineDelimiter(getBody(actual));
  }

  @RepeatedTest(10)
  void should_throw_exception_when_invalid_body() {
    // given
    PrepareOutboundPacket expected = PrepareOutboundPacketFixture.create(HeaderFixture.random(), new TestInvalidUser());

    // when, then
    assertThatCode(() -> channel.writeOutbound(expected)).satisfies(throwable -> {
      assertThat(throwable).isInstanceOf(EncoderException.class);
      assertThat(ExceptionUtils.findCause(throwable, ProblemCodeException.class))
        .isInstanceOf(ProblemCodeException.class);
      assertThat(ExceptionUtils.findCause(throwable, OutboundPacketEncodeFailException.class))
        .isInstanceOf(OutboundPacketEncodeFailException.class);
    });
  }

  private byte[] getBody(ByteBuf actual) {
    byte[] body = new byte[actual.readableBytes()];
    actual.readBytes(body);
    return body;
  }

  private void equalsBody(byte[] body, TestUser given) throws DeserializeFailedException, CryptoException {
    assertThat(multiMapper.read(ReadCommand.of(EncodingType.JSON, crypto.decrypt(body), TestUser.class)))
      .usingRecursiveComparison().isEqualTo(given);
    assertThat((char) body[body.length - 2]).isEqualTo('\r');
    assertThat((char) body[body.length - 1]).isEqualTo('\n');
  }

  private void checkLineDelimiter(byte[] body) {
    assertThat((char) body[body.length - 2]).isEqualTo('\r');
    assertThat((char) body[body.length - 1]).isEqualTo('\n');
  }

  private static class TestInvalidUser {

  }
}