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
import io.github.ppzxc.codec.model.Header;
import io.github.ppzxc.codec.model.InboundMessage;
import io.github.ppzxc.codec.model.InboundMessageFixture;
import io.github.ppzxc.crypto.AsymmetricKeyFactory;
import io.github.ppzxc.crypto.Crypto;
import io.github.ppzxc.crypto.CryptoException;
import io.github.ppzxc.crypto.CryptoFactory;
import io.github.ppzxc.crypto.CryptoProvider;
import io.github.ppzxc.crypto.Transformation;
import io.github.ppzxc.fixh.ByteArrayUtils;
import io.github.ppzxc.fixh.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.base64.Base64;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class IntegrationTest {

  public static final String LENGTH_FIELD_BASE_FRAME_DECODER = "LengthFieldBaseFrameDecoder";
  public static final String ENCRYPTED_INBOUND_MESSAGE_DECODER = "EncryptedInboundMessageDecoder";
  public static final String HAND_SHAKE_HANDLER = "HandShakeHandler";
  private static Crypto RSA_CRYPTO;
  private static Crypto AES_CRYPTO;
  private static Mapper MAPPER;
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

  @Test
  void should_return_OK() throws CryptoException {
    // given
    ByteBuf given = HandshakeFixture.withBody(RSA_CRYPTO.encrypt(ByteArrayUtils.giveMeOne(16 + 16)));

    // when
    channel.writeInbound(given);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readInt()).isEqualTo(1);
    assertThat(actual.readByte()).isEqualTo(CodecProblemCode.OK.getCode());
  }

  @Test
  void should_return_decrypted_inbound_message() throws CryptoException {
    // given
    Crypto crypto = handshake();
    byte[] given = StringUtils.giveMeOne(512).getBytes(StandardCharsets.UTF_8);
    InboundMessage expected = InboundMessageFixture.create(given);

    // when
    ByteBuf encryptedMessage = encryption(crypto, expected);
    channel.writeInbound(encryptedMessage);
    InboundMessage actual = channel.readInbound();

    // then
    assertThat(actual.header())
      .usingRecursiveComparison()
      .ignoringFields("length")
      .isEqualTo(expected.header());
    assertThat(actual.getBody()).isEqualTo(given);
  }

  private Crypto handshake() throws CryptoException {
    byte[] key = ByteArrayUtils.giveMeOne(32);
    byte[] iv = ByteArrayUtils.giveMeOne(16);
    Crypto crypto = CryptoFactory.aes(key, Transformation.AES_CBC_PKCS7PADDING, CryptoProvider.BOUNCY_CASTLE, iv);
    channel.writeInbound(HandshakeFixture.withBody(RSA_CRYPTO.encrypt(Unpooled.copiedBuffer(iv, key).array())));
    channel.readOutbound();
    return crypto;
  }

  public ByteBuf encryption(Crypto crypto, InboundMessage inboundMessage) throws CryptoException {
    ByteBuf buffer = Unpooled.buffer(inboundMessage.getBody().length + Header.ID_FIELD_LENGTH + Header.PROTOCOL_FIELDS_LENGTH);
    buffer.writeLong(inboundMessage.header().getId());
    buffer.writeByte(inboundMessage.header().getType());
    buffer.writeByte(inboundMessage.header().getStatus());
    buffer.writeByte(inboundMessage.header().getEncoding());
    buffer.writeByte(inboundMessage.header().getReserved());
    buffer.writeBytes(inboundMessage.getBody());
    byte[] cipherText = crypto.encrypt(buffer.array());
    ByteBuf buffer2 = Unpooled.buffer();
    buffer2.writeInt(cipherText.length + Header.LINE_DELIMITER_LENGTH);
    buffer2.writeBytes(cipherText);
    buffer2.writeBytes(Header.LINE_DELIMITER);
    return buffer2;
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
      AES_CRYPTO = CryptoFactory.aes(symmetricKey, Transformation.AES_CBC_PKCS7PADDING, CryptoProvider.BOUNCY_CASTLE,
        ivParameter);
      return AES_CRYPTO;
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
