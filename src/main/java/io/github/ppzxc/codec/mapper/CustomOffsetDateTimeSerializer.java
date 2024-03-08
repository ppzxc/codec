package io.github.ppzxc.codec.mapper;

import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;
import java.time.format.DateTimeFormatter;

public class CustomOffsetDateTimeSerializer extends OffsetDateTimeSerializer {

  public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSXXX");
  public static final OffsetDateTimeSerializer INSTANCE = new CustomOffsetDateTimeSerializer(FORMATTER);

  protected CustomOffsetDateTimeSerializer(DateTimeFormatter formatter) {
    super(OffsetDateTimeSerializer.INSTANCE, false, false, formatter);
  }
}