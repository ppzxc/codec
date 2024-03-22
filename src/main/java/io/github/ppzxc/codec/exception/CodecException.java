package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.CodecCode;

public class CodecException extends Exception {

  public static final long DEFAULT_ID = 0L;
  public static final String DEFAULT_REJECT = "[UNRECOGNIZED]";
  public static final String INSTANCE_DECODER = "/decoder";
  public static final String INSTANCE_HANDSHAKE = "/handshake";
  public static final String INSTANCE_ENCODER = "/encoder";
  private static final long serialVersionUID = -927664338530046046L;
  private final long id;
  private final String rejectedValue;
  private final String instance;
  private final transient Object[] args;
  private final CodecCode codecCode;

  public CodecException(long id, String rejectedValue, String instance, Object[] args,
    CodecCode codecCode) {
    this.id = id;
    this.rejectedValue = rejectedValue;
    this.instance = instance;
    this.args = args;
    this.codecCode = codecCode;
  }

  public CodecException(String message, long id, String rejectedValue, String instance, Object[] args,
    CodecCode codecCode) {
    super(message);
    this.id = id;
    this.rejectedValue = rejectedValue;
    this.instance = instance;
    this.args = args;
    this.codecCode = codecCode;
  }

  public CodecException(String message, Throwable cause, long id, String rejectedValue, String instance,
    Object[] args, CodecCode codecCode) {
    super(message, cause);
    this.id = id;
    this.rejectedValue = rejectedValue;
    this.instance = instance;
    this.args = args;
    this.codecCode = codecCode;
  }

  public CodecException(Throwable cause, long id, String rejectedValue, String instance, Object[] args,
    CodecCode codecCode) {
    super(cause);
    this.id = id;
    this.rejectedValue = rejectedValue;
    this.instance = instance;
    this.args = args;
    this.codecCode = codecCode;
  }

  public CodecException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
    long id, String rejectedValue, String instance, Object[] args, CodecCode codecCode) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.id = id;
    this.rejectedValue = rejectedValue;
    this.instance = instance;
    this.args = args;
    this.codecCode = codecCode;
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

  public CodecCode getCodecCode() {
    return codecCode;
  }
}
