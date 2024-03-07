package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

/**
 * The type Outbound packet serialize failed exception.
 */
public class OutboundPacketSerializeFailedException extends ProblemCodeException {

  private static final long serialVersionUID = -6232495018253307271L;

  /**
   * Instantiates a new Outbound packet serialize failed exception.
   *
   * @param header the header
   */
  public OutboundPacketSerializeFailedException(Header header) {
    super("outbound packet serialize failed", header.getId(), ProblemCode.OUTBOUND_PACKET_SERIALIZE_FAILED);
  }

  /**
   * Instantiates a new Outbound packet serialize failed exception.
   *
   * @param header    the header
   * @param throwable the throwable
   */
  public OutboundPacketSerializeFailedException(Header header, Throwable throwable) {
    super("outbound packet serialize failed", throwable, header.getId(), ProblemCode.OUTBOUND_PACKET_SERIALIZE_FAILED);
  }

  /**
   * Instantiates a new Outbound packet serialize failed exception.
   *
   * @param id the id
   */
  public OutboundPacketSerializeFailedException(int id) {
    super(id, ProblemCode.OUTBOUND_PACKET_SERIALIZE_FAILED);
  }

  /**
   * Instantiates a new Outbound packet serialize failed exception.
   *
   * @param message the message
   */
  public OutboundPacketSerializeFailedException(String message) {
    super(message, 0, ProblemCode.OUTBOUND_PACKET_SERIALIZE_FAILED);
  }

  /**
   * Instantiates a new Outbound packet serialize failed exception.
   *
   * @param id      the id
   * @param message the message
   */
  public OutboundPacketSerializeFailedException(int id, String message) {
    super(message, id, ProblemCode.OUTBOUND_PACKET_SERIALIZE_FAILED);
  }
}
