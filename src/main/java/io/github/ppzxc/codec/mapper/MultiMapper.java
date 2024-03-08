package io.github.ppzxc.codec.mapper;

import io.github.ppzxc.codec.exception.DeserializeFailedException;
import io.github.ppzxc.codec.exception.SerializeFailedException;

public interface MultiMapper {

  <T> T read(ReadCommand<T> command) throws DeserializeFailedException;

  <T> byte[] write(WriteCommand command) throws SerializeFailedException;
}
