package io.github.ppzxc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.List;

public final class Constants {

  private Constants() {
  }

  public static final class Crypto {

    public static final int[] SYMMETRIC_KEY_SIZE = new int[]{16, 24, 32};
  }

  public static final class LineDelimiter {

    public static byte[] BYTE_ARRAY = new byte[]{'\r', '\n'};
    public static final ByteBuf BYTE_BUF = Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer(BYTE_ARRAY));
    public static int LENGTH = BYTE_ARRAY.length;
  }

  public static final class CodecNames {

    public static final String HANDSHAKE_IDLE_HANDLER = "HandshakeIdleHandler";
    public static final String HANDSHAKE_TIMEOUT_HANDLER = "HandshakeTimeoutHandler";
    public static final String HANDSHAKE_FRAME_DECODER = "HandshakeFrameDecoder";
    public static final String HANDSHAKE_CHANNEL_HANDLER = "HandshakeChannelHandler";
    public static final List<String> HANDSHAKES;
    public static final String PROTOCOL_IDLE_HANDLER = "ProtocolIdleHandler";
    public static final String PROTOCOL_TIMEOUT_HANDLER = "ProtocolTimeoutHandler";
    public static final String PROTOCOL_FRAME_DECODER = "ProtocolFrameDecoder";
    public static final String PROTOCOL_INBOUND_DECODER = "ProtocolInboundDecoder";
    public static final String PROTOCOL_OUTBOUND_ENCODER = "ProtocolOutboundEncoder";

    static {
      HANDSHAKES = new ArrayList<>();
      HANDSHAKES.add(HANDSHAKE_IDLE_HANDLER);
      HANDSHAKES.add(HANDSHAKE_TIMEOUT_HANDLER);
      HANDSHAKES.add(HANDSHAKE_FRAME_DECODER);
      HANDSHAKES.add(HANDSHAKE_CHANNEL_HANDLER);
    }
  }
}
