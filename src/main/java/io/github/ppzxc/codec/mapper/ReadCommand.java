package io.github.ppzxc.codec.mapper;

import io.github.ppzxc.codec.model.EncodingType;
import io.github.ppzxc.fixh.ObjectUtils;

/**
 * The type Read command.
 *
 * @param <T> the type parameter
 */
public class ReadCommand<T> {

  private final EncodingType type;
  private final byte[] payload;
  private final Class<T> targetClass;

  private ReadCommand(EncodingType type, byte[] payload, Class<T> targetClass) {
    this.type = ObjectUtils.requireNonNull(type, "'type' is require non null");
    this.payload = ObjectUtils.requireNonNull(payload, "'payload' is require non null");
    this.targetClass = ObjectUtils.requireNonNull(targetClass, "'targetClass' is require non null");
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
   * Get payload byte [ ].
   *
   * @return the byte [ ]
   */
  public byte[] getPayload() {
    return payload;
  }

  /**
   * Gets target class.
   *
   * @return the target class
   */
  public Class<T> getTargetClass() {
    return targetClass;
  }

  /**
   * Of read command.
   *
   * @param <T>     the type parameter
   * @param type    the type
   * @param payload the payload
   * @param tClass  the t class
   * @return the read command
   */
  public static <T> ReadCommand<T> of(EncodingType type, byte[] payload, Class<T> tClass) {
    return new ReadCommand<>(type, payload, tClass);
  }
}
