package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

/**
 * The type Outbound packet encrypt failed exception.
 */
public class OutboundPacketEncryptFailedException extends ProblemCodeException {

  private static final long serialVersionUID = 7988106657265674464L;

  /**
   * Instantiates a new Outbound packet encrypt failed exception.
   *
   * @param header the header
   */
  public OutboundPacketEncryptFailedException(Header header) {
    super("outbound packet encrypt failed", header.getId(), ProblemCode.OUTBOUND_PACKET_ENCRYPT_FAILED);
  }

  /**
   * Instantiates a new Outbound packet encrypt failed exception.
   *
   * @param header    the header
   * @param throwable the throwable
   */
  public OutboundPacketEncryptFailedException(Header header, Throwable throwable) {
    super("outbound packet encrypt failed", throwable, header.getId(), ProblemCode.OUTBOUND_PACKET_ENCRYPT_FAILED);
  }

  /**
   * Instantiates a new Outbound packet encrypt failed exception.
   *
   * @param id the id
   */
  public OutboundPacketEncryptFailedException(int id) {
    super(id, ProblemCode.OUTBOUND_PACKET_ENCRYPT_FAILED);
  }

  /**
   * Instantiates a new Outbound packet encrypt failed exception.
   *
   * @param message the message
   */
  public OutboundPacketEncryptFailedException(String message) {
    super(message, 0, ProblemCode.OUTBOUND_PACKET_ENCRYPT_FAILED);
  }

  /**
   * Instantiates a new Outbound packet encrypt failed exception.
   *
   * @param id      the id
   * @param message the message
   */
  public OutboundPacketEncryptFailedException(int id, String message) {
    super(message, id, ProblemCode.OUTBOUND_PACKET_ENCRYPT_FAILED);
  }
}
