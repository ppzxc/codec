package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

public class OutboundPacketToRawFailedException extends ProblemCodeException {

  private static final long serialVersionUID = 6110764279170219771L;

  public OutboundPacketToRawFailedException(Header header) {
    super("outbound packet to raw packet failed", header.getId(), ProblemCode.OUTBOUND_PACKET_TO_RAW_PACKET_FAILED);
  }

  public OutboundPacketToRawFailedException(Header header, Throwable throwable) {
    super("outbound packet to raw packet failed", throwable, header.getId(),
      ProblemCode.OUTBOUND_PACKET_TO_RAW_PACKET_FAILED);
  }

  public OutboundPacketToRawFailedException(int id) {
    super(id, ProblemCode.OUTBOUND_PACKET_TO_RAW_PACKET_FAILED);
  }

  public OutboundPacketToRawFailedException(String message) {
    super(message, 0, ProblemCode.OUTBOUND_PACKET_TO_RAW_PACKET_FAILED);
  }

  public OutboundPacketToRawFailedException(int id, String message) {
    super(message, id, ProblemCode.OUTBOUND_PACKET_TO_RAW_PACKET_FAILED);
  }
}
