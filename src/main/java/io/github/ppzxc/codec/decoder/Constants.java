package io.github.ppzxc.codec.decoder;

import java.util.ArrayList;
import java.util.List;

public class Constants {

  public static final String HANDSHAKE_IDLE_STATE_HANDLER = "HandshakeIdleStateHandler";
  public static final String HANDSHAKE_TIMEOUT_STATE_HANDLER = "HandshakeTimeoutStateHandler";
  public static final String HANDSHAKE_FRAME_DECODER = "HandshakeFrameDecoder";
  public static final String HANDSHAKE_HANDLER = "HandshakeHandler";
  public static final List<String> HANDSHAKES;

  public static final String ENCRYPTED_INBOUND_MESSAGE_DECODER = "EncryptedInboundMessageDecoder";
  public static final String OUTBOUND_MESSAGE_ENCODER = "OutboundMessageEncoder";

  static {
    HANDSHAKES = new ArrayList<>();
    HANDSHAKES.add(HANDSHAKE_IDLE_STATE_HANDLER);
    HANDSHAKES.add(HANDSHAKE_TIMEOUT_STATE_HANDLER);
    HANDSHAKES.add(HANDSHAKE_FRAME_DECODER);
    HANDSHAKES.add(HANDSHAKE_HANDLER);
  }
}
