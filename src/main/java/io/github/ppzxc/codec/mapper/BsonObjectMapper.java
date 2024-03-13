package io.github.ppzxc.codec.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.undercouch.bson4jackson.BsonFactory;
import io.github.ppzxc.codec.exception.DeserializeFailedProblemException;
import io.github.ppzxc.codec.exception.SerializeFailedProblemException;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.TimeZone;

/**
 * The type Bson object mapper.
 */
public class BsonObjectMapper implements Mapper {

  private final ObjectMapper objectMapper;

  private BsonObjectMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  private BsonObjectMapper() {
    this.objectMapper = new ObjectMapper(new BsonFactory());
    this.objectMapper.setTimeZone(TimeZone.getDefault());
    this.objectMapper.setLocale(Locale.getDefault());
    this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    JavaTimeModule module = new JavaTimeModule();
    module.addSerializer(OffsetDateTime.class, CustomOffsetDateTimeSerializer.INSTANCE);
    this.objectMapper.registerModule(module);
  }

  @Override
  public <T> T read(byte[] payload, Class<T> tClass) throws DeserializeFailedProblemException {
    try {
      return objectMapper.readValue(payload, tClass);
    } catch (IOException e) {
      throw new DeserializeFailedProblemException(e);
    }
  }

  @Override
  public <T> byte[] write(T payload) throws SerializeFailedProblemException {
    try {
      return objectMapper.writeValueAsBytes(payload);
    } catch (IOException e) {
      throw new SerializeFailedProblemException(e);
    }
  }

  /**
   * Create bson object mapper.
   *
   * @param objectMapper the object mapper
   * @return the bson object mapper
   */
  public static BsonObjectMapper create(ObjectMapper objectMapper) {
    return new BsonObjectMapper(objectMapper);
  }

  /**
   * Create bson object mapper.
   *
   * @return the bson object mapper
   */
  public static BsonObjectMapper create() {
    return new BsonObjectMapper();
  }
}
