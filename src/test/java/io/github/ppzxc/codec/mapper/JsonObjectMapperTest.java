package io.github.ppzxc.codec.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ppzxc.codec.exception.DeserializeFailedException;
import io.github.ppzxc.codec.exception.SerializeFailedException;
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
  void should_serialize() throws SerializeFailedException {
    // given
    TestUser given = TestUser.random();

    // when
    byte[] actual = multiMapper.write(WriteCommand.of(EncodingType.JSON, given));

    // then
    assertThat(actual).isNotEmpty();
  }

  @Test
  void should_deserialize() throws SerializeFailedException, DeserializeFailedException {
    // given
    TestUser expected = TestUser.random();

    // when
    byte[] given = multiMapper.write(WriteCommand.of(EncodingType.JSON, expected));
    TestUser actual = multiMapper.read(ReadCommand.of(EncodingType.JSON, given, TestUser.class));

    // then
    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }
}