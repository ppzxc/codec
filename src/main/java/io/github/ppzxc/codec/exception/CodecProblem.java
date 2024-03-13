package io.github.ppzxc.codec.exception;

/**
 * The enum Codec problem.
 */
public enum CodecProblem {
  /**
   * Inbound data null pointer codec problem.
   */
  INBOUND_DATA_NULL_POINTER,
  /**
   * Less than minimum message length codec problem.
   */
  LESS_THAN_MINIMUM_MESSAGE_LENGTH,
  /**
   * Corrupted body length codec problem.
   */
  CORRUPTED_BODY_LENGTH,
  /**
   * Not same length codec problem.
   */
  NOT_SAME_LENGTH,
  /**
   * Missing line delimiter codec problem.
   */
  MISSING_LINE_DELIMITER,
  /**
   * Not supported body length codec problem.
   */
  NOT_SUPPORTED_BODY_LENGTH,
  /**
   * Handshake decode fail codec problem.
   */
  HANDSHAKE_DECODE_FAIL,
  /**
   * Outbound message encode failed codec problem.
   */
  OUTBOUND_MESSAGE_ENCODE_FAILED,
}
