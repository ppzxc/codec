package io.github.ppzxc.codec.encoder;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ppzxc.codec.exception.DeserializeFailedException;
import io.github.ppzxc.codec.exception.SerializeFailedException;
import io.github.ppzxc.codec.model.EncryptedOutboundPacket;
import io.github.ppzxc.codec.model.EncryptedOutboundPacketFixture;
import io.github.ppzxc.codec.model.HeaderFixture;
import io.github.ppzxc.codec.model.RawOutboundPacket;
import io.github.ppzxc.codec.service.Mapper;
import io.github.ppzxc.codec.service.ObjectOutputStreamMapper;
import io.github.ppzxc.crypto.Crypto;
import io.github.ppzxc.crypto.CryptoException;
import io.github.ppzxc.crypto.CryptoFactory;
import io.github.ppzxc.fixh.IntUtils;
import io.github.ppzxc.fixh.StringUtils;
import io.netty.channel.embedded.EmbeddedChannel;
import java.io.Serializable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

class OutboundPacketToRawPacketEncoderTest {

  private static Mapper MAPPER;
  private static Crypto CRYPTO;
  private EmbeddedChannel channel;

  @BeforeAll
  static void beforeAll() {
    MAPPER = new ObjectOutputStreamMapper();
    CRYPTO = CryptoFactory.aes128();
  }

  @BeforeEach
  void setUp() {
    channel = new EmbeddedChannel();
    channel.pipeline().addLast(new OutboundPacketToRawPacketEncoder());
  }

  @RepeatedTest(10)
  void should_encode_when_encrypted_packet()
    throws CryptoException, DeserializeFailedException, SerializeFailedException {
    // given
    TestCompany expected = new TestCompany(StringUtils.giveMeOne(), StringUtils.giveMeOne(), IntUtils.giveMeOne());
    EncryptedOutboundPacket given = EncryptedOutboundPacketFixture.create(HeaderFixture.random(),
      CRYPTO.encrypt(MAPPER.write(expected)));

    // when
    channel.writeOutbound(given);
    RawOutboundPacket actual = channel.readOutbound();

    // then
    assertThat(actual.getHeader())
      .usingRecursiveComparison()
      .ignoringFields("bodyLength")
      .isEqualTo(given.getHeader());
    assertThat(actual.getHeader().getBodyLength()).isEqualTo(actual.getBody().readableBytes());
    assertThat(MAPPER.read(CRYPTO.decrypt(actual.getBody().array()), TestCompany.class))
      .usingRecursiveComparison()
      .isEqualTo(expected);
  }

  private static class TestCompany implements Serializable {

    private static final long serialVersionUID = 9176751176297965951L;
    private final String name;
    private final String status;
    private final int serialNumber;

    public TestCompany(String name, String status, int serialNumber) {
      this.name = name;
      this.status = status;
      this.serialNumber = serialNumber;
    }

    public String getName() {
      return name;
    }

    public String getStatus() {
      return status;
    }

    public int getSerialNumber() {
      return serialNumber;
    }
  }
}