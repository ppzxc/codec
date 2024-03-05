package io.github.ppzxc.codec.encoder;

import io.github.ppzxc.codec.service.Mapper;

/**
 * The type Object output stream serialize encoder.
 */
public class OutboundPacketSerializeEncoder extends AbstractPrepareOutboundPacketSerializeEncoder {

  private final Mapper mapper;

  /**
   * Instantiates a new Object output stream serialize encoder.
   *
   * @param mapper the mapper
   */
  public OutboundPacketSerializeEncoder(Mapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public byte[] write(Object object) {
    return mapper.write(object);
  }
}
