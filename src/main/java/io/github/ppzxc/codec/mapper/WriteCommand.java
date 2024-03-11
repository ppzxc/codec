package io.github.ppzxc.codec.mapper;

import io.github.ppzxc.codec.model.EncodingType;
import io.github.ppzxc.fixh.ObjectUtils;

/**
 * The type Mapper write command.
 */
public final class WriteCommand {

  private final EncodingType type;
  private final Object payload;

  /**
   * Instantiates a new Mapper write command.
   *
   * @param type    the type
   * @param payload the payload
   */
  private WriteCommand(EncodingType type, Object payload) {
    this.type = ObjectUtils.requireNonNull(type, "'type' is require non null");
    this.payload = ObjectUtils.requireNonNull(payload, "'payload' is require non null");
  }

  /**
   * Of mapper write command.
   *
   * @param type    the type
   * @param payload the payload
   * @return the mapper write command
   */
  public static WriteCommand of(EncodingType type, Object payload) {
    return new WriteCommand(type, payload);
  }

  /**
   * Gets type.
   *
   * @return the type
   */
  public EncodingType getType() {
    return type;
  }

  /**
   * Gets payload.
   *
   * @return the payload
   */
  public Object getPayload() {
    return payload;
  }
}
