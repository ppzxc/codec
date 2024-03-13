package io.github.ppzxc.codec.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Fixed constructor length field based frame decoder.
 */
public class FixedConstructorLengthFieldBasedFrameDecoder extends LengthFieldBasedFrameDecoder {

  private static final Logger log = LoggerFactory.getLogger(FixedConstructorLengthFieldBasedFrameDecoder.class);

  /**
   * Instantiates a new Fixed constructor length field based frame decoder.
   *
   * @param maxFrameLength      the max frame length
   * @param lengthFieldOffset   the length field offset
   * @param lengthFieldLength   the length field length
   * @param lengthAdjustment    the length adjustment
   * @param initialBytesToStrip the initial bytes to strip
   * @param failFast            the fail fast
   */
  public FixedConstructorLengthFieldBasedFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
    int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
    super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
  }

  /**
   * Default configuration fixed constructor length field based frame decoder.
   *
   * @return the fixed constructor length field based frame decoder
   */
  public static FixedConstructorLengthFieldBasedFrameDecoder defaultConfiguration() {
    return new FixedConstructorLengthFieldBasedFrameDecoder(1024 * 1024 * 4, 8, 4, 0, 0, true);
  }

  @Override
  protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
    log.debug("{} decode", ctx.channel());
    return super.decode(ctx, in);
  }
}
