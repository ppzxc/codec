package io.github.ppzxc.codec.mapper;

import io.github.ppzxc.codec.exception.DeserializeFailedException;
import io.github.ppzxc.codec.exception.SerializeFailedException;
import io.github.ppzxc.fixh.ObjectUtils;

public class DefaultMultiMapper extends AbstractMultiMapper {

  private final Mapper jsonMapper;
  private final Mapper bsonMapper;
  private final Mapper javaMapper;
  private final Mapper protobufMapper;

  private DefaultMultiMapper(Mapper jsonMapper, Mapper bsonMapper, Mapper javaMapper, Mapper protobufMapper) {
    this.jsonMapper = ObjectUtils.requireNonNull(jsonMapper, "'jsonMapper' is require non null");
    this.bsonMapper = ObjectUtils.requireNonNull(bsonMapper, "'bsonMapper' is require non null");
    this.javaMapper = ObjectUtils.requireNonNull(javaMapper, "'javaMapper' is require non null");
    this.protobufMapper = protobufMapper;
  }

  public static MultiMapper create() {
    return DefaultMultiMapper.builder()
      .jsonMapper(JsonObjectMapper.create())
      .bsonMapper(BsonObjectMapper.create())
      .javaMapper(JavaObjectMapper.create())
      .build();
  }

  public static MultiMapper create(Mapper protobufMapper) {
    return DefaultMultiMapper.builder()
      .jsonMapper(JsonObjectMapper.create())
      .bsonMapper(BsonObjectMapper.create())
      .javaMapper(JavaObjectMapper.create())
      .protobufMapper(protobufMapper)
      .build();
  }

  public static DefaultMultiMapperBuilder builder() {
    return new DefaultMultiMapperBuilder();
  }

  @Override
  <T> T readFromProtoBuf(byte[] payload, Class<T> tClass) throws DeserializeFailedException {
    if (protobufMapper == null) {
      throw new DeserializeFailedException("not supported protobuf encoding");
    } else {
      return protobufMapper.read(payload, tClass);
    }
  }

  @Override
  <T> T readFromJson(byte[] payload, Class<T> tClass) throws DeserializeFailedException {
    return jsonMapper.read(payload, tClass);
  }

  @Override
  <T> T readFromBson(byte[] payload, Class<T> tClass) throws DeserializeFailedException {
    return bsonMapper.read(payload, tClass);
  }

  @Override
  <T> T readFromJava(byte[] payload, Class<T> tClass) throws DeserializeFailedException {
    return javaMapper.read(payload, tClass);
  }

  @Override
  <T> byte[] writeUsingProtoBuf(T payload) throws SerializeFailedException {
    if (protobufMapper == null) {
      throw new SerializeFailedException("not supported protobuf encoding");
    } else {
      return protobufMapper.write(payload);
    }
  }

  @Override
  <T> byte[] writeUsingJson(T payload) throws SerializeFailedException {
    return jsonMapper.write(payload);
  }

  @Override
  <T> byte[] writeUsingBson(T payload) throws SerializeFailedException {
    return bsonMapper.write(payload);
  }

  @Override
  <T> byte[] writeUsingJava(T payload) throws SerializeFailedException {
    return javaMapper.write(payload);
  }

  public static final class DefaultMultiMapperBuilder {

    private Mapper jsonMapper;
    private Mapper bsonMapper;
    private Mapper javaMapper;
    private Mapper protobufMapper;

    private DefaultMultiMapperBuilder() {
    }

    public DefaultMultiMapperBuilder jsonMapper(Mapper jsonMapper) {
      this.jsonMapper = jsonMapper;
      return this;
    }

    public DefaultMultiMapperBuilder bsonMapper(Mapper bsonMapper) {
      this.bsonMapper = bsonMapper;
      return this;
    }

    public DefaultMultiMapperBuilder javaMapper(Mapper javaMapper) {
      this.javaMapper = javaMapper;
      return this;
    }

    public DefaultMultiMapperBuilder protobufMapper(Mapper protobufMapper) {
      this.protobufMapper = protobufMapper;
      return this;
    }

    public DefaultMultiMapper build() {
      return new DefaultMultiMapper(jsonMapper, bsonMapper, javaMapper, protobufMapper);
    }
  }
}
