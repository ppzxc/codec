package io.github.ppzxc.codec.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.ppzxc.codec.exception.DeserializeFailedException;
import io.github.ppzxc.codec.exception.SerializeFailedException;
import java.io.IOException;
import java.util.Locale;
import java.util.TimeZone;

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
  public <T> T read(byte[] payload, Class<T> tClass) throws DeserializeFailedException {
    try {
      return objectMapper.readValue(payload, tClass);
    } catch (IOException e) {
      throw new DeserializeFailedException(e);
    }
  }

  @Override
  public <T> byte[] write(T payload) throws SerializeFailedException {
    try {
      return objectMapper.writeValueAsBytes(payload);
    } catch (IOException e) {
      throw new SerializeFailedException(e);
    }
  }

  public static JsonObjectMapper create(ObjectMapper objectMapper) {
    return new JsonObjectMapper(objectMapper);
  }

  public static JsonObjectMapper create() {
    return new JsonObjectMapper();
  }
}
