package io.github.ppzxc.codec.model;

import io.github.ppzxc.codec.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public final class HandshakeResult {

  public static int LENGTH_FIELD_LENGTH = 4;
  public static int RESULT_FIELD_LENGTH = 1;
  public static int BODY_LENGTH = RESULT_FIELD_LENGTH + Constants.LineDelimiter.LENGTH;
  public static int LENGTH = LENGTH_FIELD_LENGTH + RESULT_FIELD_LENGTH + Constants.LineDelimiter.LENGTH;
  public static int LENGTH_WITHOUT_LENGTH_FIELD = RESULT_FIELD_LENGTH + Constants.LineDelimiter.LENGTH;

  private HandshakeResult() {
  }

  public static ByteBuf of(CodecCode codecCode) {
    ByteBuf buffer = Unpooled.buffer(LENGTH);
    buffer.writeInt(LENGTH_WITHOUT_LENGTH_FIELD);
    buffer.writeByte(codecCode.getCode());
    buffer.writeBytes(Constants.LineDelimiter.BYTE_ARRAY);
    return buffer;
  }
}
