package io.github.ppzxc.codec.mapper;

import io.github.ppzxc.codec.exception.DeserializeFailedException;
import io.github.ppzxc.codec.exception.SerializeFailedException;

public interface Mapper {

  <T> T read(ReadCommand<T> command) throws DeserializeFailedException;

  byte[] write(WriteCommand command) throws SerializeFailedException;
}
