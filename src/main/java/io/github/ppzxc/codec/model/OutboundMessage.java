package io.github.ppzxc.codec.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.github.ppzxc.codec.model.OutboundMessage.Builder;

/**
 * The type Outbound message.
 */
@JsonDeserialize(builder = Builder.class)
public class OutboundMessage extends AbstractMessage {

  private static final long serialVersionUID = 8111315643882184475L;
  private final Object body;

  private OutboundMessage(Header header, Object body) {
    super(header);
    this.body = body;
  }

  @Override
  public Header getHeader() {
    return header;
  }

  /**
   * Gets body.
   *
   * @return the body
   */
  public Object getBody() {
    return body;
  }

  /**
   * Builder builder.
   *
   * @return the builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * The type Builder.
   */
  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {

    private Header header;
    private Object body;

    private Builder() {
    }

    /**
     * Header builder.
     *
     * @param header the header
     * @return the builder
     */
    public Builder header(Header header) {
      this.header = header;
      return this;
    }

    /**
     * Body builder.
     *
     * @param body the body
     * @return the builder
     */
    public Builder body(Object body) {
      this.body = body;
      return this;
    }

    /**
     * Build outbound message.
     *
     * @return the outbound message
     */
    public OutboundMessage build() {
      return new OutboundMessage(header, body);
    }
  }
}
