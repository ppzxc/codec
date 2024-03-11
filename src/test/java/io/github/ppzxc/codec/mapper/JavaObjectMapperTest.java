package io.github.ppzxc.codec.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.github.ppzxc.codec.exception.DeserializeFailedException;
import io.github.ppzxc.codec.exception.SerializeFailedException;
import io.github.ppzxc.codec.model.TestUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JavaObjectMapperTest {

  private Mapper mapper;

  @BeforeEach
  void setUp() {
    mapper = JavaObjectMapper.create();
  }

  @Test
  void should_equals_when_serialize_and_deserialize() throws SerializeFailedException, DeserializeFailedException {
    // given
    TestUser expected = TestUser.random();

    // when
    byte[] serialize = mapper.write(expected);
    TestUser actual = mapper.read(serialize, TestUser.class);

    // then
    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void should_throw_exception_when_deserialize() {
    assertThatCode(() -> mapper.read(null, TestUser.class))
      .isInstanceOf(DeserializeFailedException.class);
  }

  @Test
  void should_throw_exception_when_serialize() {
    assertThatCode(() -> mapper.write(new Object()))
      .isInstanceOf(SerializeFailedException.class);
  }
}