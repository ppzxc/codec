package io.github.ppzxc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.TooLongFrameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Fixed constructor length field based frame decoder.
 */
public class FixedConstructorLengthFieldBasedFrameDecoder extends LengthFieldBasedFrameDecoder {

  private final static Logger log = LoggerFactory.getLogger(FixedConstructorLengthFieldBasedFrameDecoder.class);

  /**
   * Creates a new instance.
   *
   * @param maxFrameLength      the maximum length of the frame.  If the length of the frame is greater than this value,
   *                            {@link TooLongFrameException} will be thrown.
   * @param lengthFieldOffset   the offset of the length field
   * @param lengthFieldLength   the length of the length field
   * @param lengthAdjustment    the compensation value to add to the value of the length field
   * @param initialBytesToStrip the number of first bytes to strip out from the decoded frame
   * @param failFast            If <tt>true</tt>, a {@link TooLongFrameException} is thrown as soon as the decoder
   *                            notices the length of the frame will exceed
   *                            <tt>maxFrameLength</tt> regardless of whether the entire frame
   *                            has been read.  If <tt>false</tt>, a {@link TooLongFrameException} is thrown after the
   *                            entire frame that exceeds <tt>maxFrameLength</tt> has been read.
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
    log.debug("{} decode", ctx.channel().toString());
    return super.decode(ctx, in);
  }
}
