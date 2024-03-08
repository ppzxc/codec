package io.github.ppzxc.codec.decoder;

import io.github.ppzxc.codec.model.RawInboundPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;

public class RawInboundPacketDecoder extends MessageToMessageDecoder<RawInboundPacket> {

  @Override
  protected void decode(ChannelHandlerContext ctx, RawInboundPacket msg, List<Object> out) throws Exception {

  }
}
