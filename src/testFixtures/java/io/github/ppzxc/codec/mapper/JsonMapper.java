package io.github.ppzxc.codec.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.ppzxc.codec.exception.DeserializeException;
import io.github.ppzxc.codec.exception.SerializeException;
import io.github.ppzxc.codec.model.EncodingType;
import java.io.IOException;
import java.util.Locale;
import java.util.TimeZone;

public class JsonMapper implements Mapper {

  private final ObjectMapper objectMapper;

  private JsonMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  private JsonMapper() {
    this.objectMapper = new ObjectMapper();
    this.objectMapper.setTimeZone(TimeZone.getDefault());
    this.objectMapper.setLocale(Locale.getDefault());
    this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  public static JsonMapper create(ObjectMapper objectMapper) {
    return new JsonMapper(objectMapper);
  }

  public static JsonMapper create() {
    return new JsonMapper();
  }

  @Override
  public <T> T read(ReadCommand<T> command) throws DeserializeException {
    if (command.getType() != EncodingType.JSON) {
      throw new IllegalArgumentException(command.getType().toString());
    }
    try {
      return objectMapper.readValue(command.getPayload(), command.getTargetClass());
    } catch (IOException e) {
      throw new DeserializeException(e);
    }
  }

  @Override
  public byte[] write(WriteCommand command) throws SerializeException {
    if (command.getType() != EncodingType.JSON) {
      throw new IllegalArgumentException(command.getType().toString());
    }
    try {
      return objectMapper.writeValueAsBytes(command.getPayload());
    } catch (IOException e) {
      throw new SerializeException(e);
    }
  }
}
