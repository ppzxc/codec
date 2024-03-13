package io.github.ppzxc.codec.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ppzxc.codec.exception.DeserializeFailedProblemException;
import io.github.ppzxc.codec.exception.SerializeFailedProblemException;
import io.github.ppzxc.codec.model.EncodingType;
import io.github.ppzxc.codec.model.TestUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonObjectMapperTest {

  private MultiMapper multiMapper;

  @BeforeEach
  void setUp() {
    multiMapper = DefaultMultiMapper.builder()
      .jsonMapper(JsonObjectMapper.create())
      .bsonMapper(BsonObjectMapper.create())
      .javaMapper(JavaObjectMapper.create())
      .build();
  }

  @Test
  void should_serialize() throws SerializeFailedProblemException {
    // given
    TestUser given = TestUser.random();

    // when
    byte[] actual = multiMapper.write(WriteCommand.of(EncodingType.JSON, given));

    // then
    assertThat(actual).isNotEmpty();
  }

  @Test
  void should_deserialize() throws SerializeFailedProblemException, DeserializeFailedProblemException {
    // given
    TestUser expected = TestUser.random();

    // when
    byte[] given = multiMapper.write(WriteCommand.of(EncodingType.JSON, expected));
    TestUser actual = multiMapper.read(ReadCommand.of(EncodingType.JSON, given, TestUser.class));

    // then
    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void should_throw_exception_when_serialize() {
    // given
    Object given = new Object();

    // when, then
    assertThatCode(() -> multiMapper.write(WriteCommand.of(EncodingType.JSON, given)))
      .isInstanceOf(SerializeFailedProblemException.class);
  }

  @Test
  void should_throw_exception_when_non_setting_mapper() {
    // given
    Object given = new Object();

    // when, then
    assertThatCode(() -> JsonObjectMapper.create(new ObjectMapper()).write(WriteCommand.of(EncodingType.JSON, given)))
      .isInstanceOf(SerializeFailedProblemException.class);
  }
}