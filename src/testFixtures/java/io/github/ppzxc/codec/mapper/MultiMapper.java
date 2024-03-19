package io.github.ppzxc.codec.mapper;

import io.github.ppzxc.codec.exception.DeserializeFailedException;
import io.github.ppzxc.codec.exception.SerializeFailedException;

public final class MultiMapper implements Mapper {

  private final Mapper jsonMapper;
  private final Mapper bsonMapper;
  private final Mapper javaSerializeMapper;
  private final Mapper protocolBufferMapper;

  private MultiMapper() {
    this.jsonMapper = JsonMapper.create();
    this.bsonMapper = BsonMapper.create();
    this.javaSerializeMapper = JavaSerializeMapper.create();
    this.protocolBufferMapper = new ProtocolBufferMapper();
  }

  public static Mapper create() {
    return new MultiMapper();
  }

  @Override
  public <T> T read(ReadCommand<T> command) throws DeserializeFailedException {
    switch (command.getType()) {
      case PROTOBUF:
        return protocolBufferMapper.read(command);
      case JSON:
        return jsonMapper.read(command);
      case BSON:
        return bsonMapper.read(command);
      case JAVA_SERIALIZE:
        return javaSerializeMapper.read(command);
      default:
        throw new IllegalArgumentException(command.getType().toString());
    }
  }

  @Override
  public byte[] write(WriteCommand command) throws SerializeFailedException {
    switch (command.getType()) {
      case PROTOBUF:
        return protocolBufferMapper.write(command);
      case JSON:
        return jsonMapper.write(command);
      case BSON:
        return bsonMapper.write(command);
      case JAVA_SERIALIZE:
        return javaSerializeMapper.write(command);
      default:
        throw new IllegalArgumentException(command.getType().toString());
    }
  }
}
