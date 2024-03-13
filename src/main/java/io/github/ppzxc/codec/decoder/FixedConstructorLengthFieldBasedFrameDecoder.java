package io.github.ppzxc.codec.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixedConstructorLengthFieldBasedFrameDecoder extends LengthFieldBasedFrameDecoder {

  private static final Logger log = LoggerFactory.getLogger(FixedConstructorLengthFieldBasedFrameDecoder.class);

  public FixedConstructorLengthFieldBasedFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
    int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
    super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
  }

  public static FixedConstructorLengthFieldBasedFrameDecoder defaultConfiguration() {
    return new FixedConstructorLengthFieldBasedFrameDecoder(1024 * 1024 * 4, 8, 4, 0, 0, true);
  }

  @Override
  protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
    log.debug("{} decode", ctx.channel());
    return super.decode(ctx, in);
  }
}
