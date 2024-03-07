package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

/**
 * The type Outbound packet to raw failed exception.
 */
public class OutboundPacketToRawFailedException extends ProblemCodeException {

  private static final long serialVersionUID = 6110764279170219771L;

  /**
   * Instantiates a new Outbound packet to raw failed exception.
   *
   * @param header the header
   */
  public OutboundPacketToRawFailedException(Header header) {
    super("outbound packet to raw packet failed", header.getId(), ProblemCode.OUTBOUND_PACKET_TO_RAW_PACKET_FAILED);
  }

  /**
   * Instantiates a new Outbound packet to raw failed exception.
   *
   * @param header    the header
   * @param throwable the throwable
   */
  public OutboundPacketToRawFailedException(Header header, Throwable throwable) {
    super("outbound packet to raw packet failed", throwable, header.getId(),
      ProblemCode.OUTBOUND_PACKET_TO_RAW_PACKET_FAILED);
  }

  /**
   * Instantiates a new Outbound packet to raw failed exception.
   *
   * @param id the id
   */
  public OutboundPacketToRawFailedException(int id) {
    super(id, ProblemCode.OUTBOUND_PACKET_TO_RAW_PACKET_FAILED);
  }

  /**
   * Instantiates a new Outbound packet to raw failed exception.
   *
   * @param message the message
   */
  public OutboundPacketToRawFailedException(String message) {
    super(message, 0, ProblemCode.OUTBOUND_PACKET_TO_RAW_PACKET_FAILED);
  }

  /**
   * Instantiates a new Outbound packet to raw failed exception.
   *
   * @param id      the id
   * @param message the message
   */
  public OutboundPacketToRawFailedException(int id, String message) {
    super(message, id, ProblemCode.OUTBOUND_PACKET_TO_RAW_PACKET_FAILED);
  }
}
