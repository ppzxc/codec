package io.github.ppzxc.codec.decoder;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ppzxc.codec.encoder.OutboundMessageEncoder;
import io.github.ppzxc.codec.mapper.Mapper;
import io.github.ppzxc.codec.mapper.MultiMapper;
import io.github.ppzxc.codec.model.CodecProblemCode;
import io.github.ppzxc.codec.model.EncryptionMode;
import io.github.ppzxc.codec.model.EncryptionPadding;
import io.github.ppzxc.codec.model.EncryptionType;
import io.github.ppzxc.codec.model.HandshakeFixture;
import io.github.ppzxc.codec.model.HandshakeHeader;
import io.github.ppzxc.crypto.AsymmetricKeyFactory;
import io.github.ppzxc.crypto.Crypto;
import io.github.ppzxc.crypto.CryptoFactory;
import io.github.ppzxc.crypto.CryptoProvider;
import io.github.ppzxc.crypto.Transformation;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
      .addLast(LENGTH_FIELD_BASE_FRAME_DECODER, FixedConstructorLengthFieldBasedFrameDecoder.defaultConfiguration());
    channel.pipeline().addLast(HAND_SHAKE_HANDLER, new TestHandshakeHandler(RSA_CRYPTO));
  }

  @Test
  void should() {
    // given
    ByteBuf given = HandshakeFixture.wrongHandShakeType();

    // when
    channel.writeInbound(given);
    ByteBuf actual = channel.readOutbound();

    // then
    assertThat(actual.readInt()).isEqualTo(1);
    assertThat(actual.readByte()).isEqualTo(CodecProblemCode.INVALID_HAND_SHAKE_TYPE.getCode());
  }

  private static class TestHandshakeHandler extends AbstractHandshakeHandler {

    protected TestHandshakeHandler(Crypto rsaCrypto) {
      super(rsaCrypto);
    }

    @Override
    public Crypto getAesCrypto(HandshakeHeader handShakeHeader, byte[] ivParameter, byte[] symmetricKey)
      throws Exception {
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
