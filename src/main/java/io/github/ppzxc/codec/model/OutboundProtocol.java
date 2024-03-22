package io.github.ppzxc.codec.model;

public class OutboundProtocol extends AbstractProtocol {

  private static final long serialVersionUID = 8111315643882184475L;
  private final Object body;

  private OutboundProtocol(Header header, Object body) {
    super(header);
    this.body = body;
  }

  public Object body() {
    return body;
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public String toString() {
    return "OutboundMessage{header=" + header() + ", body=[MASKED]}";
  }

  public static final class Builder {

    private Header header;
    private Object body;

    private Builder() {
    }

    public Builder header(Header header) {
      this.header = header;
      return this;
    }

    public Builder body(Object body) {
      this.body = body;
      return this;
    }

    public OutboundProtocol build() {
      return new OutboundProtocol(header, body);
    }
  }
}
