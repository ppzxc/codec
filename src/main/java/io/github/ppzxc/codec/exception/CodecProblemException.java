package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.CodecProblemCode;

public class CodecProblemException extends Exception {

  private static final long serialVersionUID = -927664338530046046L;
  private final long id;
  private final String rejectedValue;
  private final String instance;
  private final transient Object[] args;
  private final CodecProblemCode codecProblemCode;

  public CodecProblemException(long id, String rejectedValue, String instance, Object[] args,
    CodecProblemCode codecProblemCode) {
    this.id = id;
    this.rejectedValue = rejectedValue;
    this.instance = instance;
    this.args = args;
    this.codecProblemCode = codecProblemCode;
  }

  public CodecProblemException(String message, long id, String rejectedValue, String instance, Object[] args,
    CodecProblemCode codecProblemCode) {
    super(message);
    this.id = id;
    this.rejectedValue = rejectedValue;
    this.instance = instance;
    this.args = args;
    this.codecProblemCode = codecProblemCode;
  }

  public CodecProblemException(String message, Throwable cause, long id, String rejectedValue, String instance,
    Object[] args, CodecProblemCode codecProblemCode) {
    super(message, cause);
    this.id = id;
    this.rejectedValue = rejectedValue;
    this.instance = instance;
    this.args = args;
    this.codecProblemCode = codecProblemCode;
  }

  public CodecProblemException(Throwable cause, long id, String rejectedValue, String instance, Object[] args,
    CodecProblemCode codecProblemCode) {
    super(cause);
    this.id = id;
    this.rejectedValue = rejectedValue;
    this.instance = instance;
    this.args = args;
    this.codecProblemCode = codecProblemCode;
  }

  public CodecProblemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
    long id, String rejectedValue, String instance, Object[] args, CodecProblemCode codecProblemCode) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.id = id;
    this.rejectedValue = rejectedValue;
    this.instance = instance;
    this.args = args;
    this.codecProblemCode = codecProblemCode;
  }

  public long getId() {
    return id;
  }

  public String getRejectedValue() {
    return rejectedValue;
  }

  public String getInstance() {
    return instance;
  }

  public Object[] getArgs() {
    return args;
  }

  public CodecProblemCode getCodecProblemCode() {
    return codecProblemCode;
  }
}
