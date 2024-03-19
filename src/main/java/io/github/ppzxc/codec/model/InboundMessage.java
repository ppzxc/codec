package io.github.ppzxc.codec.model;

import io.github.ppzxc.fixh.ObjectUtils;

public class InboundMessage extends AbstractMessage {

  private static final long serialVersionUID = -8173604055646973726L;
  private final byte[] body;

  private InboundMessage(Header header, byte[] body) {
    super(header);
    this.body = body;
  }

  public static Builder builder() {
    return new Builder();
  }

  public byte[] getBody() {
    return body;
  }

  @Override
  public String toString() {
    return "InboundMessage{header=" + header() + ", body=[MASKED]}";
  }

  public static final class Builder {

    private Header header;
    private byte[] body;

    private Builder() {
    }

    public Builder header(Header header) {
      this.header = ObjectUtils.requireNonNull(header, "'Header' is require non null");
      return this;
    }

    public Builder body(byte[] body) {
      this.body = ObjectUtils.requireNonNull(body, "'Body' is require non null");
      return this;
    }

    public InboundMessage build() {
      return new InboundMessage(header, body);
    }
  }
}
