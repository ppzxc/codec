package io.github.ppzxc.codec.mapper;

import io.github.ppzxc.codec.model.EncodingType;
import io.github.ppzxc.fixh.ObjectUtils;

public final class ReadCommand<T> {

  private final EncodingType type;
  private final byte[] payload;
  private final Class<T> targetClass;

  private ReadCommand(EncodingType type, byte[] payload, Class<T> targetClass) {
    this.type = ObjectUtils.requireNonNull(type, "'type' is require non null");
    this.payload = ObjectUtils.requireNonNull(payload, "'payload' is require non null");
    this.targetClass = ObjectUtils.requireNonNull(targetClass, "'targetClass' is require non null");
  }

  public EncodingType getType() {
    return type;
  }

  public byte[] getPayload() {
    return payload;
  }

  public Class<T> getTargetClass() {
    return targetClass;
  }

  public static <T> ReadCommand<T> of(EncodingType type, byte[] payload, Class<T> tClass) {
    return new ReadCommand<>(type, payload, tClass);
  }
}
