package io.github.ppzxc.codec.mapper;

import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;
import java.time.format.DateTimeFormatter;

/**
 * The type Custom offset date time serializer.
 */
public class CustomOffsetDateTimeSerializer extends OffsetDateTimeSerializer {

  /**
   * The constant FORMATTER.
   */
  public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSXXX");
  /**
   * The constant INSTANCE.
   */
  public static final OffsetDateTimeSerializer INSTANCE = new CustomOffsetDateTimeSerializer(FORMATTER);
  private static final long serialVersionUID = -1815788788365367027L;

  /**
   * Instantiates a new Custom offset date time serializer.
   *
   * @param formatter the formatter
   */
  protected CustomOffsetDateTimeSerializer(DateTimeFormatter formatter) {
    super(OffsetDateTimeSerializer.INSTANCE, false, false, formatter);
  }
}
