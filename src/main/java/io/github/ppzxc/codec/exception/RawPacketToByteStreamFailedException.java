package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

/**
 * The type Raw packet to byte stream failed exception.
 */
public class RawPacketToByteStreamFailedException extends ProblemCodeException {

  private static final long serialVersionUID = 6110764279170219771L;

  /**
   * Instantiates a new Raw packet to byte stream failed exception.
   *
   * @param header the header
   */
  public RawPacketToByteStreamFailedException(Header header) {
    super("outbound packet to byte stream failed", header.getId(), ProblemCode.RAW_PACKET_TO_BYTE_STREAM_FAILED);
  }

  /**
   * Instantiates a new Raw packet to byte stream failed exception.
   *
   * @param header    the header
   * @param throwable the throwable
   */
  public RawPacketToByteStreamFailedException(Header header, Throwable throwable) {
    super("outbound packet to byte stream failed", throwable, header.getId(),
      ProblemCode.RAW_PACKET_TO_BYTE_STREAM_FAILED);
  }

  /**
   * Instantiates a new Raw packet to byte stream failed exception.
   *
   * @param id the id
   */
  public RawPacketToByteStreamFailedException(int id) {
    super(id, ProblemCode.RAW_PACKET_TO_BYTE_STREAM_FAILED);
  }

  /**
   * Instantiates a new Raw packet to byte stream failed exception.
   *
   * @param message the message
   */
  public RawPacketToByteStreamFailedException(String message) {
    super(message, 0, ProblemCode.RAW_PACKET_TO_BYTE_STREAM_FAILED);
  }

  /**
   * Instantiates a new Raw packet to byte stream failed exception.
   *
   * @param id      the id
   * @param message the message
   */
  public RawPacketToByteStreamFailedException(int id, String message) {
    super(message, id, ProblemCode.RAW_PACKET_TO_BYTE_STREAM_FAILED);
  }
}
