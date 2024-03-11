package io.github.ppzxc.codec.mapper;

import io.github.ppzxc.codec.exception.DeserializeFailedException;
import io.github.ppzxc.codec.exception.NotSupportedEncodingTypeException;
import io.github.ppzxc.codec.exception.SerializeFailedException;
import io.github.ppzxc.codec.model.EncodingType;
import io.github.ppzxc.fixh.ObjectUtils;

/**
 * The type Default multi mapper.
 */
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

  /**
   * Create multi mapper.
   *
   * @return the multi mapper
   */
  public static MultiMapper create() {
    return DefaultMultiMapper.builder()
      .jsonMapper(JsonObjectMapper.create())
      .bsonMapper(BsonObjectMapper.create())
      .javaMapper(JavaObjectMapper.create())
      .build();
  }

  /**
   * Create multi mapper.
   *
   * @param protobufMapper the protobuf mapper
   * @return the multi mapper
   */
  public static MultiMapper create(Mapper protobufMapper) {
    return DefaultMultiMapper.builder()
      .jsonMapper(JsonObjectMapper.create())
      .bsonMapper(BsonObjectMapper.create())
      .javaMapper(JavaObjectMapper.create())
      .protobufMapper(protobufMapper)
      .build();
  }

  /**
   * Builder default multi mapper builder.
   *
   * @return the default multi mapper builder
   */
  public static DefaultMultiMapperBuilder builder() {
    return new DefaultMultiMapperBuilder();
  }

  @Override
  <T> T readFromProtoBuf(byte[] payload, Class<T> tClass) throws DeserializeFailedException {
    if (protobufMapper == null) {
      throw new DeserializeFailedException(new NotSupportedEncodingTypeException(EncodingType.PROTOBUF));
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
      throw new SerializeFailedException(new NotSupportedEncodingTypeException(EncodingType.PROTOBUF));
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

  /**
   * The type Default multi mapper builder.
   */
  public static final class DefaultMultiMapperBuilder {

    private Mapper jsonMapper;
    private Mapper bsonMapper;
    private Mapper javaMapper;
    private Mapper protobufMapper;

    private DefaultMultiMapperBuilder() {
    }

    /**
     * Json mapper default multi mapper builder.
     *
     * @param jsonMapper the json mapper
     * @return the default multi mapper builder
     */
    public DefaultMultiMapperBuilder jsonMapper(Mapper jsonMapper) {
      this.jsonMapper = jsonMapper;
      return this;
    }

    /**
     * Bson mapper default multi mapper builder.
     *
     * @param bsonMapper the bson mapper
     * @return the default multi mapper builder
     */
    public DefaultMultiMapperBuilder bsonMapper(Mapper bsonMapper) {
      this.bsonMapper = bsonMapper;
      return this;
    }

    /**
     * Java mapper default multi mapper builder.
     *
     * @param javaMapper the java mapper
     * @return the default multi mapper builder
     */
    public DefaultMultiMapperBuilder javaMapper(Mapper javaMapper) {
      this.javaMapper = javaMapper;
      return this;
    }

    /**
     * Protobuf mapper default multi mapper builder.
     *
     * @param protobufMapper the protobuf mapper
     * @return the default multi mapper builder
     */
    public DefaultMultiMapperBuilder protobufMapper(Mapper protobufMapper) {
      this.protobufMapper = protobufMapper;
      return this;
    }

    /**
     * Build default multi mapper.
     *
     * @return the default multi mapper
     */
    public DefaultMultiMapper build() {
      return new DefaultMultiMapper(jsonMapper, bsonMapper, javaMapper, protobufMapper);
    }
  }
}
