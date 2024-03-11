package io.github.ppzxc.codec.exception;

/**
 * The enum Problem code.
 */
public enum ProblemCode {
  /**
   *Bad request problem code.
   */
  BAD_REQUEST,
  /**
   *Inbound data null pointer problem code.
   */
  INBOUND_DATA_NULL_POINTER,
  /**
   *Less than minimum message length problem code.
   */
  LESS_THAN_MINIMUM_MESSAGE_LENGTH,
  /**
   *Corrupted body length problem code.
   */
  CORRUPTED_BODY_LENGTH,
  /**
   *Not same length problem code.
   */
  NOT_SAME_LENGTH,
  /**
   *Missing line delimiter problem code.
   */
  MISSING_LINE_DELIMITER,
  /**
   *Not supported body length problem code.
   */
  NOT_SUPPORTED_BODY_LENGTH,
  /**
   *Handshake decode fail problem code.
   */
  HANDSHAKE_DECODE_FAIL,
  /**
   *Handshake decrypt failed problem code.
   */
  HANDSHAKE_DECRYPT_FAILED,
  /**
   *Handshake deserialize failed problem code.
   */
  HANDSHAKE_DESERIALIZE_FAILED,
  /**
   *Outbound message serialize failed problem code.
   */
  OUTBOUND_MESSAGE_SERIALIZE_FAILED,
  /**
   *Outbound message encrypt failed problem code.
   */
  OUTBOUND_MESSAGE_ENCRYPT_FAILED,
  /**
   *Outbound message to raw failed problem code.
   */
  OUTBOUND_MESSAGE_TO_RAW_FAILED,
  /**
   *Raw to byte stream failed problem code.
   */
  RAW_TO_BYTE_STREAM_FAILED,
  /**
   *Serialize failed problem code.
   */
  SERIALIZE_FAILED,
  /**
   *Deserialize failed problem code.
   */
  DESERIALIZE_FAILED,
  /**
   *Not supported encoding type problem code.
   */
  NOT_SUPPORTED_ENCODING_TYPE,
}
