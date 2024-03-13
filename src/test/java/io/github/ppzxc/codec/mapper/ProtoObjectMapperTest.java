package io.github.ppzxc.codec.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.github.ppzxc.codec.exception.DeserializeFailedException;
import io.github.ppzxc.codec.exception.SerializeFailedException;
import io.github.ppzxc.codec.model.EncryptionMethod;
import io.github.ppzxc.codec.model.EncryptionMethodProtobufFixture;
import io.github.ppzxc.codec.model.protobuf.EncryptionMethodProtobuf;
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
      .isInstanceOf(DeserializeFailedException.class);
  }

  @Test
  void should_deserialize_when_protobuf_2() {
    assertThatCode(() -> mapper.read(null, Object.class))
      .isInstanceOf(DeserializeFailedException.class);
  }

  @Test
  void should_deserialize_when_protobuf_3() throws DeserializeFailedException {
    // given
    EncryptionMethodProtobuf expected = EncryptionMethodProtobufFixture.random();

    // when
    EncryptionMethod actual = mapper.read(expected.toByteArray(), EncryptionMethod.class);

    // then
    assertThat(actual.getType().name()).isEqualToIgnoringCase(expected.getType().name());
    assertThat(actual.getMode().name()).isEqualToIgnoringCase(expected.getMode().name());
    assertThat(actual.getPadding().name()).isEqualToIgnoringCase(expected.getPadding().name());
    assertThat(actual.getIv()).isEqualTo(expected.getIv());
    assertThat(actual.getSymmetricKey()).isEqualTo(expected.getSymmetricKey());
  }
}