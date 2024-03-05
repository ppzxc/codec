package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

public class RawPacketToByteStreamFailedException extends ProblemCodeException {

  private static final long serialVersionUID = 6110764279170219771L;

  public RawPacketToByteStreamFailedException(Header header) {
    super("outbound packet to byte stream failed", header.getId(), ProblemCode.RAW_PACKET_TO_BYTE_STREAM_FAILED);
  }

  public RawPacketToByteStreamFailedException(Header header, Throwable throwable) {
    super("outbound packet to byte stream failed", throwable, header.getId(),
      ProblemCode.RAW_PACKET_TO_BYTE_STREAM_FAILED);
  }

  public RawPacketToByteStreamFailedException(int id) {
    super(id, ProblemCode.RAW_PACKET_TO_BYTE_STREAM_FAILED);
  }

  public RawPacketToByteStreamFailedException(String message) {
    super(message, 0, ProblemCode.RAW_PACKET_TO_BYTE_STREAM_FAILED);
  }

  public RawPacketToByteStreamFailedException(int id, String message) {
    super(message, id, ProblemCode.RAW_PACKET_TO_BYTE_STREAM_FAILED);
  }
}
