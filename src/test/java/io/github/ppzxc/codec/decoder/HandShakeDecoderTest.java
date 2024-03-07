package io.github.ppzxc.codec.decoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.github.ppzxc.codec.exception.HandShakeDecodeFailException;
import io.github.ppzxc.codec.exception.ProblemCodeException;
import io.github.ppzxc.codec.exception.SerializeFailedException;
import io.github.ppzxc.codec.model.EncryptedHandShakePacket;
import io.github.ppzxc.codec.model.EncryptedHandShakePacketFixture;
import io.github.ppzxc.codec.model.EncryptionMethod;
import io.github.ppzxc.codec.model.EncryptionMethodFixture;
import io.github.ppzxc.codec.model.HandShakePacket;
import io.github.ppzxc.codec.service.Mapper;
import io.github.ppzxc.codec.service.ObjectOutputStreamMapper;
import io.github.ppzxc.crypto.AsymmetricKeyFactory;
import io.github.ppzxc.crypto.Crypto;
import io.github.ppzxc.crypto.CryptoException;
import io.github.ppzxc.crypto.CryptoFactory;
import io.github.ppzxc.fixh.ByteArrayUtils;
import io.github.ppzxc.fixh.ExceptionUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DecoderException;
import java.io.EOFException;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

class HandShakeDecoderTest {

  private static KeyPair keyPair;
  private Crypto crypto;
  private Mapper mapper;
  private EmbeddedChannel channel;

  @BeforeAll
  static void beforeAll() throws NoSuchAlgorithmException, NoSuchProviderException {
    keyPair = AsymmetricKeyFactory.generateRsa();
  }

  @BeforeEach
  void setUp() {
    crypto = CryptoFactory.rsa(keyPair.getPublic(), keyPair.getPrivate());
    mapper = new ObjectOutputStreamMapper();
    channel = new EmbeddedChannel();
    channel.pipeline().addLast(new HandShakeDecoder(crypto, mapper));
  }

  @RepeatedTest(10)
  void should_throw_exception_when_empty_body() {
    // given
    EncryptedHandShakePacket given = EncryptedHandShakePacketFixture.withBody(Unpooled.buffer(0));

    // when, then
    assertThatCode(() -> channel.writeInbound(given)).satisfies(throwable -> {
      assertThat(throwable).isInstanceOf(DecoderException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(EOFException.class);
      assertThat(ExceptionUtils.findCause(throwable, ProblemCodeException.class))
        .isInstanceOf(ProblemCodeException.class);
      assertThat(ExceptionUtils.findCause(throwable, HandShakeDecodeFailException.class))
        .isInstanceOf(HandShakeDecodeFailException.class);
    });
  }

  @RepeatedTest(10)
  void should_throw_exception_when_invalid_body() {
    // given
    byte[] expected = ByteArrayUtils.giveMeOne(1024);
    EncryptedHandShakePacket given = EncryptedHandShakePacketFixture.withBody(Unpooled.wrappedBuffer(expected));

    // when, then
    assertThatCode(() -> channel.writeInbound(given)).satisfies(throwable -> {
      assertThat(throwable).isInstanceOf(DecoderException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOfAny(IOException.class,
        ArrayIndexOutOfBoundsException.class);
      assertThat(ExceptionUtils.findCause(throwable, ProblemCodeException.class))
        .isInstanceOf(ProblemCodeException.class);
      assertThat(ExceptionUtils.findCause(throwable, HandShakeDecodeFailException.class))
        .isInstanceOf(HandShakeDecodeFailException.class);
    });
  }

  @RepeatedTest(10)
  void should_return_HandShakePacket() throws SerializeFailedException, CryptoException {
    // given
    EncryptionMethod expected = EncryptionMethodFixture.random();
    byte[] plainText = mapper.write(expected);
    byte[] cipherText = crypto.encrypt(plainText);
    EncryptedHandShakePacket given = EncryptedHandShakePacketFixture.withBody(Unpooled.wrappedBuffer(cipherText));

    // when
    channel.writeInbound(given);
    HandShakePacket actual = channel.readInbound();

    // then
    assertThat(actual.getHeader()).usingRecursiveComparison().isEqualTo(given.getHeader());
    assertThat(actual.getEncryptionMethod()).usingRecursiveComparison().isEqualTo(expected);
  }
}