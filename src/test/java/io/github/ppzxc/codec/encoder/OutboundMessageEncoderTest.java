package io.github.ppzxc.codec.encoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.ppzxc.codec.exception.SerializeFailedException;
import io.github.ppzxc.codec.mapper.Mapper;
import io.github.ppzxc.codec.model.Header;
import io.github.ppzxc.codec.model.HeaderFixture;
import io.github.ppzxc.codec.model.LineDelimiter;
import io.github.ppzxc.codec.model.OutboundMessage;
import io.github.ppzxc.codec.model.OutboundMessageFixture;
import io.github.ppzxc.crypto.Crypto;
import io.github.ppzxc.crypto.CryptoException;
import io.github.ppzxc.crypto.CryptoProvider;
import io.github.ppzxc.fixh.ByteArrayUtils;
import io.github.ppzxc.fixh.ExceptionUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.EncoderException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

class OutboundMessageEncoderTest {

  private Crypto crypto;
  private Mapper mapper;
  private EmbeddedChannel channel;

  @BeforeAll
  static void beforeAll() {
    CryptoProvider.BOUNCY_CASTLE.addProvider();
  }

  @BeforeEach
  void setUp() {
    crypto = mock(Crypto.class);
    mapper = mock(Mapper.class);
    channel = new EmbeddedChannel();
    channel.pipeline().addLast(new OutboundMessageEncoder(crypto, mapper));
  }

  @RepeatedTest(10)
  void should_ByteBuf_when_null_body() {
    // given
    OutboundMessage expected = OutboundMessageFixture.create(HeaderFixture.create(), null);

    // when
    channel.writeOutbound(expected);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(Header.MINIMUM_LENGTH);
    assertThat(actual.readInt()).isEqualTo(LineDelimiter.LENGTH);
    assertThat(actual.readLong()).isEqualTo(expected.header().getId());
    assertThat(actual.readByte()).isEqualTo(expected.header().getType());
    assertThat(actual.readByte()).isEqualTo(expected.header().getStatus());
    assertThat(actual.readByte()).isEqualTo(expected.header().getEncoding());
    assertThat(actual.readByte()).isEqualTo(expected.header().getReserved());
    byte[] body = new byte[actual.readableBytes()];
    actual.readBytes(body);
    assertThat(body).isEqualTo(LineDelimiter.BYTE_ARRAY);
  }

  @RepeatedTest(10)
  void should_throw_SerializeFailedException() throws SerializeFailedException {
    // given
    OutboundMessage expected = OutboundMessageFixture.create(HeaderFixture.create(), new Object());

    // when, then
    when(mapper.write(any())).thenThrow(new SerializeFailedException());
    assertThatCode(() -> channel.writeOutbound(expected)).satisfies(throwable -> {
      assertThat(throwable).isInstanceOf(EncoderException.class);
      assertThat(ExceptionUtils.findCause(throwable, SerializeFailedException.class)).isInstanceOf(
        SerializeFailedException.class);
    });
  }

  @RepeatedTest(10)
  void should_throw_CryptoException() throws SerializeFailedException, CryptoException {
    // given
    OutboundMessage expected = OutboundMessageFixture.create(HeaderFixture.create(), new Object());

    // when, then
    when(mapper.write(any())).thenReturn(new byte[0]);
    when(crypto.encrypt((byte[]) any())).thenThrow(new CryptoException());
    assertThatCode(() -> channel.writeOutbound(expected)).satisfies(throwable -> {
      assertThat(throwable).isInstanceOf(EncoderException.class);
      assertThat(ExceptionUtils.findCause(throwable, CryptoException.class)).isInstanceOf(
        CryptoException.class);
    });
  }

  @RepeatedTest(10)
  void should_return_ByteBuf_when_exists_body() throws SerializeFailedException, CryptoException {
    // given
    byte[] expectedBody = ByteArrayUtils.giveMeOne(128);
    OutboundMessage expected = OutboundMessageFixture.create(HeaderFixture.create(), new Object());

    // when
    when(mapper.write(any())).thenReturn(new byte[0]);
    when(crypto.encrypt((byte[]) any())).thenReturn(expectedBody);
    channel.writeOutbound(expected);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(expectedBody.length + Header.MINIMUM_LENGTH);
    int length = actual.readInt();
    assertThat(length).isEqualTo(expectedBody.length + LineDelimiter.LENGTH);
    assertThat(actual.readLong()).isEqualTo(expected.header().getId());
    assertThat(actual.readByte()).isEqualTo(expected.header().getType());
    assertThat(actual.readByte()).isEqualTo(expected.header().getStatus());
    assertThat(actual.readByte()).isEqualTo(expected.header().getEncoding());
    assertThat(actual.readByte()).isEqualTo(expected.header().getReserved());
    byte[] body = new byte[actual.readableBytes() - LineDelimiter.BYTE_ARRAY.length];
    actual.readBytes(body);
    assertThat(body).isEqualTo(expectedBody);
    byte[] lineDelimiter = new byte[LineDelimiter.BYTE_ARRAY.length];
    actual.readBytes(lineDelimiter);
    assertThat(lineDelimiter).isEqualTo(LineDelimiter.BYTE_ARRAY);
  }
}