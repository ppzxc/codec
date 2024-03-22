package io.github.ppzxc.codec.mapper;

import io.github.ppzxc.codec.exception.DeserializeException;
import io.github.ppzxc.codec.exception.SerializeException;

public interface Mapper {

  <T> T read(ReadCommand<T> command) throws DeserializeException;

  byte[] write(WriteCommand command) throws SerializeException;
}
