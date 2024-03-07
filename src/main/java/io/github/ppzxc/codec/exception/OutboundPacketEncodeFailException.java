package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

/**
 * The type Outbound packet encode fail exception.
 */
public class OutboundPacketEncodeFailException extends ProblemCodeException {

  private static final long serialVersionUID = -2797875406038916760L;

  /**
   * Instantiates a new Outbound packet encode fail exception.
   *
   * @param header    the header
   * @param throwable the throwable
   */
  public OutboundPacketEncodeFailException(Header header, Throwable throwable) {
    super("outbound packet encrypt failed", throwable, header.getId(), ProblemCode.OUTBOUND_PACKET_ENCRYPT_FAILED);
  }
}
