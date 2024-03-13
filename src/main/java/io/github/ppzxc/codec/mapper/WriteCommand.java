package io.github.ppzxc.codec.mapper;

import io.github.ppzxc.codec.model.EncodingType;
import io.github.ppzxc.fixh.ObjectUtils;

public final class WriteCommand {

  private final EncodingType type;
  private final Object payload;

  private WriteCommand(EncodingType type, Object payload) {
    this.type = ObjectUtils.requireNonNull(type, "'type' is require non null");
    this.payload = ObjectUtils.requireNonNull(payload, "'payload' is require non null");
  }

  public static WriteCommand of(EncodingType type, Object payload) {
    return new WriteCommand(type, payload);
  }

  public EncodingType getType() {
    return type;
  }

  public Object getPayload() {
    return payload;
  }
}
