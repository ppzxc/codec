package io.github.ppzxc.codec.mapper;

import io.github.ppzxc.codec.exception.DeserializeException;
import io.github.ppzxc.codec.exception.SerializeException;
import io.github.ppzxc.codec.model.EncodingType;

public class ProtocolBufferMapper implements Mapper {

  @Override
  public <T> T read(ReadCommand<T> command) throws DeserializeException {
    if (command.getType() != EncodingType.PROTOBUF) {
      throw new IllegalArgumentException(command.getType().toString());
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public byte[] write(WriteCommand command) throws SerializeException {
    if (command.getType() != EncodingType.PROTOBUF) {
      throw new IllegalArgumentException(command.getType().toString());
    }
    throw new UnsupportedOperationException();
  }
}
