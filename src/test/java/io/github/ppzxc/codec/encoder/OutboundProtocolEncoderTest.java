package io.github.ppzxc.codec.encoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.ppzxc.codec.Constants.LineDelimiter;
import io.github.ppzxc.codec.exception.SerializeException;
import io.github.ppzxc.codec.mapper.Mapper;
import io.github.ppzxc.codec.model.Header;
import io.github.ppzxc.codec.model.HeaderFixture;
import io.github.ppzxc.codec.model.OutboundMessageFixture;
import io.github.ppzxc.codec.model.OutboundProtocol;
import io.github.ppzxc.crypto.Crypto;
import io.github.ppzxc.crypto.CryptoException;
import io.github.ppzxc.crypto.CryptoFactory;
import io.github.ppzxc.crypto.CryptoProvider;
import io.github.ppzxc.fixh.ByteArrayUtils;
import io.github.ppzxc.fixh.ExceptionUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.EncoderException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;

class OutboundProtocolEncoderTest {

  private Crypto crypto;
  private Mapper mapper;
  private EmbeddedChannel channel;

  @BeforeAll
  static void beforeAll() {
    CryptoProvider.BOUNCY_CASTLE.addProvider();
  }

  @RepeatedTest(10)
  void should_ByteBuf_when_null_body() throws CryptoException {
    // given
    setting(CryptoFactory.aes128());
    OutboundProtocol expected = OutboundMessageFixture.create(HeaderFixture.create(), null);

    // when
    channel.writeOutbound(expected);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(ByteBufUtil.equals(actual, actual.readableBytes() - 2, LineDelimiter.BYTE_BUF, 0, 2)).isTrue();
    assertThat(actual.readableBytes()).isEqualTo(Header.ENCRYPTED_EMPTY_FULL_LENGTH);
    assertThat(actual.readInt()).isEqualTo(Header.ENCRYPTED_EMPTY_BODY_LENGTH);
    byte[] cipherText = new byte[actual.readableBytes()];
    actual.readBytes(cipherText);
    ByteBuf actualBody = Unpooled.wrappedBuffer(crypto.decrypt(cipherText));
    assertThat(actualBody.readLong()).isEqualTo(expected.header().getId());
    assertThat(actualBody.readByte()).isEqualTo(expected.header().getType());
    assertThat(actualBody.readByte()).isEqualTo(expected.header().getStatus());
    assertThat(actualBody.readByte()).isEqualTo(expected.header().getEncoding());
    assertThat(actualBody.readByte()).isEqualTo(expected.header().getReserved());
  }

  @RepeatedTest(10)
  void should_throw_SerializeFailedException() throws SerializeException {
    // given
    setting(CryptoFactory.aes128());
    OutboundProtocol expected = OutboundMessageFixture.create(HeaderFixture.create(), new Object());

    // when, then
    when(mapper.write(any())).thenThrow(new SerializeException());
    assertThatCode(() -> channel.writeOutbound(expected)).satisfies(throwable -> {
      assertThat(throwable).isInstanceOf(EncoderException.class);
      assertThat(ExceptionUtils.findCause(throwable, SerializeException.class)).isInstanceOf(
        SerializeException.class);
    });
  }

  @RepeatedTest(10)
  void should_throw_CryptoException() throws SerializeException, CryptoException {
    // given
    setting(mock(Crypto.class));
    OutboundProtocol expected = OutboundMessageFixture.create(HeaderFixture.create(), new Object());

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
  void should_return_ByteBuf_when_exists_body() throws SerializeException, CryptoException {
    // given
    setting(CryptoFactory.aes128());
    byte[] expectedBody = ByteArrayUtils.giveMeOne(128);
    OutboundProtocol expected = OutboundMessageFixture.create(HeaderFixture.create(), new Object());

    // when
    when(mapper.write(any())).thenReturn(expectedBody);
    channel.writeOutbound(expected);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(ByteBufUtil.equals(actual, actual.readableBytes() - 2, LineDelimiter.BYTE_BUF, 0, 2)).isTrue();
    assertThat(actual.readInt()).isGreaterThanOrEqualTo(crypto.encrypt(expectedBody).length + LineDelimiter.LENGTH);
    byte[] cipherText = new byte[actual.readableBytes()];
    actual.readBytes(cipherText);
    ByteBuf actualBody = Unpooled.wrappedBuffer(crypto.decrypt(cipherText));
    assertThat(actualBody.readLong()).isEqualTo(expected.header().getId());
    assertThat(actualBody.readByte()).isEqualTo(expected.header().getType());
    assertThat(actualBody.readByte()).isEqualTo(expected.header().getStatus());
    assertThat(actualBody.readByte()).isEqualTo(expected.header().getEncoding());
    assertThat(actualBody.readByte()).isEqualTo(expected.header().getReserved());
    byte[] plainTextBody = new byte[actualBody.readableBytes()];
    actualBody.readBytes(plainTextBody);
    assertThat(plainTextBody).isEqualTo(expectedBody);
  }

  private void setting(Crypto crypto) {
    this.crypto = crypto;
    mapper = mock(Mapper.class);
    channel = new EmbeddedChannel();
    channel.pipeline().addLast(new OutboundProtocolEncoder(crypto, mapper));
  }
}