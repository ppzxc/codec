package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

public class OutboundPacketSerializeFailedException extends ProblemCodeException {

  private static final long serialVersionUID = -6232495018253307271L;

  public OutboundPacketSerializeFailedException(Header header) {
    super("outbound packet serialize failed", header.getId(), ProblemCode.OUTBOUND_PACKET_SERIALIZE_FAILED);
  }

  public OutboundPacketSerializeFailedException(Header header, Throwable throwable) {
    super("outbound packet serialize failed", throwable, header.getId(), ProblemCode.OUTBOUND_PACKET_SERIALIZE_FAILED);
  }

  public OutboundPacketSerializeFailedException(int id) {
    super(id, ProblemCode.OUTBOUND_PACKET_SERIALIZE_FAILED);
  }

  public OutboundPacketSerializeFailedException(String message) {
    super(message, 0, ProblemCode.OUTBOUND_PACKET_SERIALIZE_FAILED);
  }

  public OutboundPacketSerializeFailedException(int id, String message) {
    super(message, id, ProblemCode.OUTBOUND_PACKET_SERIALIZE_FAILED);
  }
}
