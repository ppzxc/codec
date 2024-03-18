package io.github.ppzxc.codec.mapper;

import io.github.ppzxc.codec.exception.DeserializeFailedException;
import io.github.ppzxc.codec.exception.SerializeFailedException;

public class ProtoObjectMapper implements Mapper {

  @Override
  public <T> T read(byte[] payload, Class<T> tClass) throws DeserializeFailedException {
    return null;
  }

  @Override
  public <T> byte[] write(T payload) throws SerializeFailedException {
    return new byte[0];
  }
}
