package io.github.ppzxc.codec.decoder;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ppzxc.codec.model.EncryptedHandShakePacketFixture;
import io.github.ppzxc.codec.model.DecryptedHandShakePacket;
import io.github.ppzxc.codec.model.EncryptedHandShakePacket;
import io.github.ppzxc.crypto.AsymmetricKeyFactory;
import io.github.ppzxc.crypto.Crypto;
import io.github.ppzxc.crypto.CryptoException;
import io.github.ppzxc.crypto.CryptoFactory;
import io.github.ppzxc.fixh.ByteArrayUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class HandShakeDecryptDecoderTest {

  private static Crypto CRYPTO;
  private EmbeddedChannel channel;

  @BeforeAll
  static void beforeAll() throws NoSuchAlgorithmException, NoSuchProviderException {
    KeyPair keyPair = AsymmetricKeyFactory.generateRsa();
    CRYPTO = CryptoFactory.rsa(keyPair.getPublic(), keyPair.getPrivate());
  }

  @BeforeEach
  void setUp() {
    channel = new EmbeddedChannel();
    channel.pipeline().addLast(new HandShakeDecryptDecoder(CRYPTO));
  }

  @RepeatedTest(10)
  void should_return_decrypted_handShakePacket_when_success_encrypt() throws CryptoException {
    // given
    byte[] givenBody = ByteArrayUtils.giveMeOne(128);
    EncryptedHandShakePacket given = EncryptedHandShakePacketFixture.withBody(
      Unpooled.wrappedBuffer(CRYPTO.encrypt(givenBody)));

    // when
    channel.writeInbound(given);
    DecryptedHandShakePacket actual = channel.readInbound();

    // then
    assertThat(actual.getHeader()).usingRecursiveComparison().isEqualTo(given.getHeader());
    assertThat(actual.getBody()).isEqualTo(givenBody);
  }
}