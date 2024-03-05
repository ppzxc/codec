package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

public class OutboundPacketEncryptFailedException extends ProblemCodeException {

  private static final long serialVersionUID = 7988106657265674464L;

  public OutboundPacketEncryptFailedException(Header header) {
    super("outbound packet encrypt failed", header.getId(), ProblemCode.OUTBOUND_PACKET_ENCRYPT_FAILED);
  }

  public OutboundPacketEncryptFailedException(Header header, Throwable throwable) {
    super("outbound packet encrypt failed", throwable, header.getId(), ProblemCode.OUTBOUND_PACKET_ENCRYPT_FAILED);
  }

  public OutboundPacketEncryptFailedException(int id) {
    super(id, ProblemCode.OUTBOUND_PACKET_ENCRYPT_FAILED);
  }

  public OutboundPacketEncryptFailedException(String message) {
    super(message, 0, ProblemCode.OUTBOUND_PACKET_ENCRYPT_FAILED);
  }

  public OutboundPacketEncryptFailedException(int id, String message) {
    super(message, id, ProblemCode.OUTBOUND_PACKET_ENCRYPT_FAILED);
  }
}
