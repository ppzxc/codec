package io.github.ppzxc.codec.encoder;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ppzxc.codec.exception.DeserializeFailedException;
import io.github.ppzxc.codec.model.PrepareOutboundPacket;
import io.github.ppzxc.codec.model.PrepareOutboundPacketFixture;
import io.github.ppzxc.codec.model.SerializedOutboundPacket;
import io.github.ppzxc.codec.service.Mapper;
import io.github.ppzxc.codec.service.ObjectOutputStreamMapper;
import io.github.ppzxc.fixh.IntUtils;
import io.github.ppzxc.fixh.RandomUtils;
import io.netty.channel.embedded.EmbeddedChannel;
import java.io.Serializable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

class OutboundPacketSerializeEncoderTest {

  private EmbeddedChannel channel;
  private Mapper mapper;

  @BeforeEach
  void setUp() {
    channel = new EmbeddedChannel();
    mapper = new ObjectOutputStreamMapper();
    channel.pipeline().addLast(new OutboundPacketSerializeEncoder(mapper));
  }

  @RepeatedTest(10)
  void should_serialized() throws DeserializeFailedException {
    // given
    TestUser expected = new TestUser(RandomUtils.getInstance().string(), RandomUtils.getInstance().string(), IntUtils.giveMeOne());
    PrepareOutboundPacket given = PrepareOutboundPacketFixture.withBody(expected);

    // when
    channel.writeOutbound(given);
    SerializedOutboundPacket actual = channel.readOutbound();

    // then
    assertThat(actual.getHeader()).usingRecursiveComparison().isEqualTo(given.getHeader());
    assertThat(mapper.read(actual.getBody(), TestUser.class)).usingRecursiveComparison().isEqualTo(expected);
  }


  private static class TestUser implements Serializable {

    private static final long serialVersionUID = -8962487339705879461L;
    private final String username;
    private final String password;
    private final int age;

    public TestUser(String username, String password, int age) {
      this.username = username;
      this.password = password;
      this.age = age;
    }

    public String getUsername() {
      return username;
    }

    public String getPassword() {
      return password;
    }

    public int getAge() {
      return age;
    }
  }
}