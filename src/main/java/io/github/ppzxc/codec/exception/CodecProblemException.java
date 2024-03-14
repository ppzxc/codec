package io.github.ppzxc.codec.exception;

public class CodecProblemException extends Exception {

  private static final long serialVersionUID = -927664338530046046L;
  private final int id;
  private final String rejectedValue;
  private final String instance;
  private final transient Object[] args;
  private final CodecProblem codecProblem;

  public CodecProblemException(int id, String rejectedValue, String instance, Object[] args,
    CodecProblem codecProblem) {
    this.id = id;
    this.rejectedValue = rejectedValue;
    this.instance = instance;
    this.args = args;
    this.codecProblem = codecProblem;
  }

  public CodecProblemException(String message, int id, String rejectedValue, String instance, Object[] args,
    CodecProblem codecProblem) {
    super(message);
    this.id = id;
    this.rejectedValue = rejectedValue;
    this.instance = instance;
    this.args = args;
    this.codecProblem = codecProblem;
  }

  public CodecProblemException(String message, Throwable cause, int id, String rejectedValue, String instance,
    Object[] args, CodecProblem codecProblem) {
    super(message, cause);
    this.id = id;
    this.rejectedValue = rejectedValue;
    this.instance = instance;
    this.args = args;
    this.codecProblem = codecProblem;
  }

  public CodecProblemException(Throwable cause, int id, String rejectedValue, String instance, Object[] args,
    CodecProblem codecProblem) {
    super(cause);
    this.id = id;
    this.rejectedValue = rejectedValue;
    this.instance = instance;
    this.args = args;
    this.codecProblem = codecProblem;
  }

  public CodecProblemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
    int id, String rejectedValue, String instance, Object[] args, CodecProblem codecProblem) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.id = id;
    this.rejectedValue = rejectedValue;
    this.instance = instance;
    this.args = args;
    this.codecProblem = codecProblem;
  }

  public int getId() {
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

  public CodecProblem getCodecProblem() {
    return codecProblem;
  }
}
