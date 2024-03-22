package io.github.ppzxc.codec.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.undercouch.bson4jackson.BsonFactory;
import io.github.ppzxc.codec.exception.DeserializeException;
import io.github.ppzxc.codec.exception.SerializeException;
import io.github.ppzxc.codec.model.EncodingType;
import java.io.IOException;
import java.util.Locale;
import java.util.TimeZone;

public class BsonMapper implements Mapper {

  private final ObjectMapper objectMapper;

  private BsonMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  private BsonMapper() {
    this.objectMapper = new ObjectMapper(new BsonFactory());
    this.objectMapper.setTimeZone(TimeZone.getDefault());
    this.objectMapper.setLocale(Locale.getDefault());
    this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    this.objectMapper.registerModule(new JavaTimeModule());
  }

  public static BsonMapper create(ObjectMapper objectMapper) {
    return new BsonMapper(objectMapper);
  }

  public static BsonMapper create() {
    return new BsonMapper();
  }

  @Override
  public <T> T read(ReadCommand<T> command) throws DeserializeException {
    if (command.getType() != EncodingType.BSON) {
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
    if (command.getType() != EncodingType.BSON) {
      throw new IllegalArgumentException(command.getType().toString());
    }
    try {
      return objectMapper.writeValueAsBytes(command.getPayload());
    } catch (IOException e) {
      throw new SerializeException(e);
    }
  }
}
