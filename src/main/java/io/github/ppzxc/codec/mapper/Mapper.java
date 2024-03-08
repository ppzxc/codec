package io.github.ppzxc.codec.mapper;

import io.github.ppzxc.codec.exception.DeserializeFailedException;
import io.github.ppzxc.codec.exception.SerializeFailedException;

public interface Mapper {

  <T> T read(byte[] payload, Class<T> tClass) throws DeserializeFailedException;

  <T> byte[] write(T payload) throws SerializeFailedException;
}
