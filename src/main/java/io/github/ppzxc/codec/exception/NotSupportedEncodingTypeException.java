package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.EncodingType;

/**
 * The type Not supported encoding type exception.
 */
public class NotSupportedEncodingTypeException extends ProblemCodeException {

  private static final long serialVersionUID = -383793409548335014L;

  /**
   * Instantiates a new Not supported encoding type exception.
   */
  public NotSupportedEncodingTypeException(EncodingType encodingType) {
    super(encodingType.toString(), 0, ProblemCode.NOT_SUPPORTED_ENCODING_TYPE);
  }
}
