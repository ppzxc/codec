package io.github.ppzxc.codec.model;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public final class LineDelimiter {

  public static byte[] BYTE_ARRAY = new byte[]{'\r', '\n'};
  public static final ByteBuf BYTE_BUF = Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer(BYTE_ARRAY));
  public static int LENGTH = BYTE_ARRAY.length;
}
