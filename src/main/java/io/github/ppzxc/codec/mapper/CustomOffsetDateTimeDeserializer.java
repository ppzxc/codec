package io.github.ppzxc.codec.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class CustomOffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {

  public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
  public static final CustomOffsetDateTimeDeserializer INSTANCE = new CustomOffsetDateTimeDeserializer();

  @Override
  public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
    throws IOException {
    return jsonParser.getText() == null ? null : OffsetDateTime.parse(jsonParser.getText(), FORMATTER);
  }
}
