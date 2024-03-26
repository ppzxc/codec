package io.github.ppzxc.codec.decoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.ppzxc.codec.encoder.OutboundProtocolEncoder;
import io.github.ppzxc.codec.mapper.Mapper;
import io.github.ppzxc.codec.model.Header;
import io.github.ppzxc.codec.model.InboundProtocol;
import io.github.ppzxc.codec.model.OutboundProtocol;
import io.github.ppzxc.crypto.Crypto;
import io.github.ppzxc.crypto.CryptoFactory;
import io.github.ppzxc.crypto.CryptoProvider;
import io.github.ppzxc.fixh.LongUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IntegratedByteBufDecoderTest {

  private Crypto crypto;
  private Mapper mapper;
  private EmbeddedChannel channel;
  private Encoder encoder;
  private ChannelHandlerContext channelHandlerContext;

  @BeforeEach
  void setUp() {
    CryptoProvider.BOUNCY_CASTLE.addProvider();
    crypto = CryptoFactory.aes128();
    channel = new EmbeddedChannel();
    channel.pipeline().addLast(CodecLengthFieldBasedFrameDecoder.defaultConfiguration());
    channel.pipeline().addLast(new ByteBufDecoder(crypto));
    encoder = new Encoder(crypto, mapper);
    channelHandlerContext = mock(ChannelHandlerContext.class);
  }

  @Test
  void should_decode() throws Exception {
    // given
    OutboundProtocol expected = OutboundProtocol.builder()
      .header(Header.builder()
        .length(0)
        .id(LongUtils.giveMeOne())
        .type((byte) 0x03)
        .status((byte) 0x00)
        .encoding((byte) 0x00)
        .reserved((byte) 0x00)
        .build())
      .body(null)
      .build();
    when(channelHandlerContext.channel()).thenReturn(null);
    ByteBuf given = encoder.encodeToResult(channelHandlerContext, expected);

    // when
    channel.writeInbound(given);
    InboundProtocol actual = channel.readInbound();

    // then
    assertThat(actual.header().getLength()).isGreaterThanOrEqualTo(Header.MINIMUM_LENGTH);
    assertThat(actual.header().getId()).isEqualTo(expected.header().getId());
    assertThat(actual.header().getType()).isEqualTo(expected.header().getType());
    assertThat(actual.header().getStatus()).isEqualTo(expected.header().getStatus());
    assertThat(actual.header().getEncoding()).isEqualTo(expected.header().getEncoding());
    assertThat(actual.header().getReserved()).isEqualTo(expected.header().getReserved());
  }

  public static class Encoder extends OutboundProtocolEncoder {

    public Encoder(Crypto crypto, Mapper mapper) {
      super(crypto, mapper);
    }

    public ByteBuf encodeToResult(ChannelHandlerContext ctx, OutboundProtocol msg) throws Exception {
      List<Object> out = new ArrayList<>();
      encode(ctx, msg, out);
      return (ByteBuf) out.get(0);
    }
  }
}