package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

public class UnsupportedBodyLengthException extends CodecProblemException {

  private static final long serialVersionUID = 5628609465590812055L;

  public UnsupportedBodyLengthException(Header header, String rejectedValue) {
    super("not supported body length: " + header.getBodyLength(), header.getId(), rejectedValue, "/decoder",
      new Object[]{rejectedValue}, CodecProblem.NOT_SUPPORTED_BODY_LENGTH);
  }
}
