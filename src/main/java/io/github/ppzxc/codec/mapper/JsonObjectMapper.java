package io.github.ppzxc.codec.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.ppzxc.codec.exception.DeserializeFailedProblemException;
import io.github.ppzxc.codec.exception.SerializeFailedProblemException;
import java.io.IOException;
import java.util.Locale;
import java.util.TimeZone;

/**
 * The type Json object mapper.
 */
public class JsonObjectMapper implements Mapper {

  private final ObjectMapper objectMapper;

  private JsonObjectMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  private JsonObjectMapper() {
    this.objectMapper = new ObjectMapper();
    this.objectMapper.setTimeZone(TimeZone.getDefault());
    this.objectMapper.setLocale(Locale.getDefault());
    this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
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
   * Create json object mapper.
   *
   * @param objectMapper the object mapper
   * @return the json object mapper
   */
  public static JsonObjectMapper create(ObjectMapper objectMapper) {
    return new JsonObjectMapper(objectMapper);
  }

  /**
   * Create json object mapper.
   *
   * @return the json object mapper
   */
  public static JsonObjectMapper create() {
    return new JsonObjectMapper();
  }
}
