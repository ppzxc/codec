package io.github.ppzxc.codec.mapper;

import io.github.ppzxc.codec.exception.DeserializeFailedException;
import io.github.ppzxc.codec.exception.NotSupportedEncodingTypeException;
import io.github.ppzxc.codec.exception.SerializeFailedException;
import io.github.ppzxc.codec.model.EncodingType;

public abstract class AbstractMultiMapper implements MultiMapper {

  abstract <T> T readFromProtoBuf(byte[] payload, Class<T> tClass) throws DeserializeFailedException;

  abstract <T> T readFromJson(byte[] payload, Class<T> tClass) throws DeserializeFailedException;

  abstract <T> T readFromBson(byte[] payload, Class<T> tClass) throws DeserializeFailedException;

  abstract <T> T readFromJava(byte[] payload, Class<T> tClass) throws DeserializeFailedException;

  abstract <T> byte[] writeUsingProtoBuf(T payload) throws SerializeFailedException;

  abstract <T> byte[] writeUsingJson(T payload) throws SerializeFailedException;

  abstract <T> byte[] writeUsingBson(T payload) throws SerializeFailedException;

  abstract <T> byte[] writeUsingJava(T payload) throws SerializeFailedException;

  @Override
  public <T> T read(ReadCommand<T> command) throws DeserializeFailedException {
    switch (command.getType()) {
      case PROTOBUF:
        return readFromProtoBuf(command.getPayload(), command.getTargetClass());
      case JSON:
        return readFromJson(command.getPayload(), command.getTargetClass());
      case BSON:
        return readFromBson(command.getPayload(), command.getTargetClass());
      case JAVA_BINARY:
        return readFromJava(command.getPayload(), command.getTargetClass());
      case NULL:
      default:
        throw new DeserializeFailedException(new NotSupportedEncodingTypeException(EncodingType.NULL));
    }
  }

  @Override
  public <T> byte[] write(WriteCommand command) throws SerializeFailedException {
    switch (command.getType()) {
      case PROTOBUF:
        return writeUsingProtoBuf(command.getPayload());
      case JSON:
        return writeUsingJson(command.getPayload());
      case BSON:
        return writeUsingBson(command.getPayload());
      case JAVA_BINARY:
        return writeUsingJava(command.getPayload());
      case NULL:
      default:
        throw new SerializeFailedException(new NotSupportedEncodingTypeException(EncodingType.NULL));
    }
  }
}
