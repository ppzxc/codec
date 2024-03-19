package io.github.ppzxc.codec.decoder;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ppzxc.codec.encoder.OutboundMessageEncoder;
import io.github.ppzxc.codec.mapper.Mapper;
import io.github.ppzxc.codec.mapper.MultiMapper;
import io.github.ppzxc.codec.model.ByteArrayFixture;
import io.github.ppzxc.codec.model.CodecProblemCode;
import io.github.ppzxc.codec.model.EncryptionMode;
import io.github.ppzxc.codec.model.EncryptionPadding;
import io.github.ppzxc.codec.model.EncryptionType;
import io.github.ppzxc.codec.model.HandshakeFixture;
import io.github.ppzxc.codec.model.HandshakeHeader;
import io.github.ppzxc.crypto.AsymmetricKeyFactory;
import io.github.ppzxc.crypto.Crypto;
import io.github.ppzxc.crypto.CryptoException;
import io.github.ppzxc.crypto.CryptoFactory;
import io.github.ppzxc.crypto.CryptoProvider;
import io.github.ppzxc.crypto.Transformation;
import io.github.ppzxc.fixh.ByteArrayUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class IntegrationTest {

  public static final String LENGTH_FIELD_BASE_FRAME_DECODER = "LengthFieldBaseFrameDecoder";
  public static final String ENCRYPTED_INBOUND_MESSAGE_DECODER = "EncryptedInboundMessageDecoder";
  public static final String HAND_SHAKE_HANDLER = "HandShakeHandler";
  private static Crypto RSA_CRYPTO;
  private static Mapper MAPPER;
  private Crypto aesCrypto;
  private EmbeddedChannel channel;

  @BeforeAll
  static void beforeAll() throws NoSuchAlgorithmException, NoSuchProviderException {
    CryptoProvider.BOUNCY_CASTLE.addProvider();
    RSA_CRYPTO = CryptoFactory.rsa(AsymmetricKeyFactory.generateRsa());
    MAPPER = MultiMapper.create();
  }

  @BeforeEach
  void setUp() {
    channel = new EmbeddedChannel();
    channel.pipeline()
      .addLast(LENGTH_FIELD_BASE_FRAME_DECODER, FixedLengthFieldBasedFrameDecoder.defaultConfiguration());
    channel.pipeline().addLast(HAND_SHAKE_HANDLER, new TestHandshakeSimpleChannelInboundHandler(RSA_CRYPTO));
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3})
  void should_return_SHORT_LENGTH(int value) {
    // given
    byte[] given = ByteArrayFixture.create(ByteArrayUtils.giveMeOne(value));

    // when
    channel.writeInbound(Unpooled.wrappedBuffer(given));
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeHeader.RESULT_LENGTH);
    assertThat(actual.readInt()).isEqualTo(1);
    assertThat(actual.readByte()).isEqualTo(CodecProblemCode.SHORT_LENGTH.getCode());
  }

  @Test
  void should_return_MISSING_LINE_DELIMITER() {
    // given
    ByteBuf given = HandshakeFixture.missingLineDelimiter(ByteArrayUtils.giveMeOne(128));

    // when
    channel.writeInbound(given);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(HandshakeHeader.RESULT_LENGTH);
    assertThat(actual.readInt()).isEqualTo(1);
    assertThat(actual.readByte()).isEqualTo(CodecProblemCode.MISSING_LINE_DELIMITER.getCode());
  }

  @Test
  void should_return_INVALID_HAND_SHAKE_TYPE() {
    // given
    ByteBuf given = HandshakeFixture.wrongHandShakeType();

    // when
    channel.writeInbound(given);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readInt()).isEqualTo(1);
    assertThat(actual.readByte()).isEqualTo(CodecProblemCode.INVALID_HAND_SHAKE_TYPE.getCode());
  }

  @Test
  void should_return_INVALID_ENCRYPTION_TYPE() {
    // given
    ByteBuf given = HandshakeFixture.wrongEncryptionType();

    // when
    channel.writeInbound(given);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readInt()).isEqualTo(1);
    assertThat(actual.readByte()).isEqualTo(CodecProblemCode.INVALID_ENCRYPTION_TYPE.getCode());
  }


  @Test
  void should_return_INVALID_ENCRYPTION_MODE() {
    // given
    ByteBuf given = HandshakeFixture.wrongEncryptionMode();

    // when
    channel.writeInbound(given);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readInt()).isEqualTo(1);
    assertThat(actual.readByte()).isEqualTo(CodecProblemCode.INVALID_ENCRYPTION_MODE.getCode());
  }

  @Test
  void should_return_INVALID_ENCRYPTION_PADDING() {
    // given
    ByteBuf given = HandshakeFixture.wrongEncryptionPadding();

    // when
    channel.writeInbound(given);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readInt()).isEqualTo(1);
    assertThat(actual.readByte()).isEqualTo(CodecProblemCode.INVALID_ENCRYPTION_PADDING.getCode());
  }

  @Test
  void should_return_DECRYPT_FAIL() {
    // given
    ByteBuf given = HandshakeFixture.wrongSymmetricKeySize();

    // when
    channel.writeInbound(given);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readInt()).isEqualTo(1);
    assertThat(actual.readByte()).isEqualTo(CodecProblemCode.DECRYPT_FAIL.getCode());
  }

  @Test
  void should_return_INVALID_KEY_SIZE() throws CryptoException {
    // given
    ByteBuf given = HandshakeFixture.withBody(RSA_CRYPTO.encrypt(ByteArrayUtils.giveMeOne(16 + 20)));

    // when
    channel.writeInbound(given);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readableBytes()).isEqualTo(5);
    assertThat(actual.readInt()).isEqualTo(1);
    assertThat(actual.readByte()).isEqualTo(CodecProblemCode.INVALID_KEY_SIZE.getCode());
  }

  @Disabled
  @Test
  void should_return_CRYPTO_CREATE_FAIL() throws CryptoException {
    // given
    ByteBuf given = HandshakeFixture.withBody(RSA_CRYPTO.encrypt(ByteArrayUtils.giveMeOne(16 + 16)));

    // when
    channel.writeInbound(given);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readInt()).isEqualTo(1);
    assertThat(actual.readByte()).isEqualTo(CodecProblemCode.CRYPTO_CREATE_FAIL.getCode());
  }

  private static class TestHandshakeSimpleChannelInboundHandler extends HandshakeSimpleChannelInboundHandler {

    protected TestHandshakeSimpleChannelInboundHandler(Crypto rsaCrypto) {
      super(rsaCrypto);
    }

    @Override
    public Crypto getAesCrypto(HandshakeHeader handShakeHeader, byte[] ivParameter, byte[] symmetricKey) {
      if (handShakeHeader.getEncryptionType() != EncryptionType.ADVANCED_ENCRYPTION_STANDARD) {
        throw new IllegalArgumentException(handShakeHeader.getEncryptionType().toString());
      }
      if (handShakeHeader.getEncryptionMode() != EncryptionMode.CIPHER_BLOCK_CHAINING) {
        throw new IllegalArgumentException(handShakeHeader.getEncryptionMode().toString());
      }
      if (handShakeHeader.getEncryptionPadding() != EncryptionPadding.PKCS7PADDING) {
        throw new IllegalArgumentException(handShakeHeader.getEncryptionPadding().toString());
      }
      return CryptoFactory.aes(symmetricKey, Transformation.AES_CBC_PKCS7PADDING, CryptoProvider.BOUNCY_CASTLE,
        ivParameter);
    }

    @Override
    public void addHandler(ChannelPipeline pipeline, Crypto crypto) {
      pipeline.addAfter(LENGTH_FIELD_BASE_FRAME_DECODER, ENCRYPTED_INBOUND_MESSAGE_DECODER,
        new EncryptedInboundMessageDecoder(crypto));
      pipeline.addAfter(ENCRYPTED_INBOUND_MESSAGE_DECODER, "ENCRYPTED_INBOUND_MESSAGE_DECODER",
        new OutboundMessageEncoder(crypto, MAPPER));
    }
  }
}
