package io.github.ppzxc.codec.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.github.ppzxc.codec.model.OutboundMessage.Builder;

@JsonDeserialize(builder = Builder.class)
public class OutboundMessage extends AbstractMessage {

  private static final long serialVersionUID = 8111315643882184475L;
  private final Object body;

  private OutboundMessage(Header header, Object body) {
    super(header);
    this.body = body;
  }

  @Override
  public Header header() {
    return header;
  }

  public Object getBody() {
    return body;
  }

  public static Builder builder() {
    return new Builder();
  }

  @JsonPOJOBuilder(withPrefix = "")
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

    public OutboundMessage build() {
      return new OutboundMessage(header, body);
    }
  }
}
