package io.github.ppzxc.codec.exception;

/**
 * The enum Problem code.
 */
public enum ProblemCode {
  /**
   * Bad request problem code.
   */
  BAD_REQUEST,
  /**
   * Inbound data null pointer problem code.
   */
  INBOUND_DATA_NULL_POINTER,
  /**
   * Less than minimum packet length problem code.
   */
  LESS_THAN_MINIMUM_PACKET_LENGTH,
  /**
   * Corrupted body length problem code.
   */
  CORRUPTED_BODY_LENGTH,
  /**
   * Not same length problem code.
   */
  NOT_SAME_LENGTH,
  /**
   * Missing line delimiter problem code.
   */
  MISSING_LINE_DELIMITER,
  /**
   * Not supported body length problem code.
   */
  NOT_SUPPORTED_BODY_LENGTH,
  /**
   * Handshake decrypt failed problem code.
   */
  HANDSHAKE_DECRYPT_FAILED,
  /**
   * Handshake deserialize failed problem code.
   */
  HANDSHAKE_DESERIALIZE_FAILED,
  /**
   * Outbound packet serialize failed problem code.
   */
  OUTBOUND_PACKET_SERIALIZE_FAILED,
  /**
   * Outbound packet encrypt failed problem code.
   */
  OUTBOUND_PACKET_ENCRYPT_FAILED,
  /**
   * Outbound packet to raw packet failed problem code.
   */
  OUTBOUND_PACKET_TO_RAW_PACKET_FAILED,
  /**
   * Raw packet to byte stream failed problem code.
   */
  RAW_PACKET_TO_BYTE_STREAM_FAILED,
  /**
   * Serialize failed problem code.
   */
  SERIALIZE_FAILED,
  /**
   * Deserialize failed problem code.
   */
  DESERIALIZE_FAILED,
}
