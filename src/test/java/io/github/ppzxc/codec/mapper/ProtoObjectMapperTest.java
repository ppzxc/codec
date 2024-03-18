package io.github.ppzxc.codec.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.github.ppzxc.codec.exception.SerializeFailedException;
import io.github.ppzxc.codec.model.EncryptionMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProtoObjectMapperTest {

  private Mapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ProtoObjectMapper();
  }

  @Test
  void should_throw_exception_when_serialize() throws SerializeFailedException {
    assertThat(mapper.write(new Object())).isEmpty();
  }

  @Test
  void should_deserialize_when_protobuf_1() {
    assertThatCode(() -> mapper.read(null, EncryptionMethod.class))
      .isNull();
  }

  @Test
  void should_deserialize_when_protobuf_2() {
    assertThatCode(() -> mapper.read(null, Object.class))
      .isNull();
  }
}