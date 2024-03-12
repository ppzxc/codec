package io.github.ppzxc.codec.decoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import io.github.ppzxc.codec.exception.HandShakeDecodeFailException;
import io.github.ppzxc.codec.exception.ProblemCodeException;
import io.github.ppzxc.codec.exception.SerializeFailedException;
import io.github.ppzxc.codec.mapper.DefaultMultiMapper;
import io.github.ppzxc.codec.mapper.MultiMapper;
import io.github.ppzxc.codec.mapper.WriteCommand;
import io.github.ppzxc.codec.model.EncodingType;
import io.github.ppzxc.codec.model.EncryptedHandShakeMessage;
import io.github.ppzxc.codec.model.EncryptedHandShakeMessageFixture;
import io.github.ppzxc.codec.model.EncryptionMethod;
import io.github.ppzxc.codec.model.EncryptionMethodFixture;
import io.github.ppzxc.codec.model.HandShakeMessage;
import io.github.ppzxc.crypto.AsymmetricKeyFactory;
import io.github.ppzxc.crypto.Crypto;
import io.github.ppzxc.crypto.CryptoException;
import io.github.ppzxc.crypto.CryptoFactory;
import io.github.ppzxc.crypto.CryptoProvider;
import io.github.ppzxc.fixh.ByteArrayUtils;
import io.github.ppzxc.fixh.ExceptionUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DecoderException;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

class EncryptedHandShakeMessageDecoderTest {

  private static KeyPair keyPair;
  private Crypto crypto;
  private MultiMapper multiMapper;
  private EmbeddedChannel channel;

  @BeforeAll
  static void beforeAll() throws NoSuchAlgorithmException, NoSuchProviderException {
    CryptoProvider.BOUNCY_CASTLE.addProvider();
    keyPair = AsymmetricKeyFactory.generateRsa(2048);
  }

  @BeforeEach
  void setUp() {
    crypto = CryptoFactory.rsa(keyPair.getPublic(), keyPair.getPrivate());
    multiMapper = DefaultMultiMapper.create();
    channel = new EmbeddedChannel();
    channel.pipeline().addLast(new EncryptedHandShakeMessageDecoder(crypto, multiMapper));
  }

  @RepeatedTest(10)
  void should_throw_exception_when_empty_body() {
    // given
    EncryptedHandShakeMessage given = EncryptedHandShakeMessageFixture.withJsonBody(Unpooled.buffer(0));

    // when, then
    assertThatCode(() -> channel.writeInbound(given)).satisfies(throwable -> {
      assertThat(throwable).isInstanceOf(DecoderException.class);
      assertThat(ExceptionUtils.getRootCause(throwable)).isInstanceOf(MismatchedInputException.class);
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
    EncryptedHandShakeMessage given = EncryptedHandShakeMessageFixture.withBody(Unpooled.wrappedBuffer(expected));

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
  void should_return_HandShakeMessage_when_json() throws SerializeFailedException, CryptoException {
    // given
    EncryptionMethod expected = EncryptionMethodFixture.random();
    byte[] plainText = multiMapper.write(WriteCommand.of(EncodingType.JSON, expected));
    byte[] cipherText = crypto.encrypt(plainText);
    EncryptedHandShakeMessage given = EncryptedHandShakeMessageFixture.withJsonBody(Unpooled.wrappedBuffer(cipherText));

    // when
    channel.writeInbound(given);
    HandShakeMessage actual = channel.readInbound();

    // then
    assertThat(actual.header()).usingRecursiveComparison().isEqualTo(given.header());
    assertThat(actual.getEncryptionMethod()).usingRecursiveComparison().isEqualTo(expected);
  }

  @RepeatedTest(10)
  void should_return_HandShakeMessage_when_bson() throws SerializeFailedException, CryptoException {
    // given
    EncryptionMethod expected = EncryptionMethodFixture.random();
    byte[] plainText = multiMapper.write(WriteCommand.of(EncodingType.BSON, expected));
    byte[] cipherText = crypto.encrypt(plainText);
    EncryptedHandShakeMessage given = EncryptedHandShakeMessageFixture.withBsonBody(Unpooled.wrappedBuffer(cipherText));

    // when
    channel.writeInbound(given);
    HandShakeMessage actual = channel.readInbound();

    // then
    assertThat(actual.header()).usingRecursiveComparison().isEqualTo(given.header());
    assertThat(actual.getEncryptionMethod()).usingRecursiveComparison().isEqualTo(expected);
  }
}