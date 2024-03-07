package io.github.ppzxc.codec.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.github.ppzxc.codec.exception.DeserializeFailedException;
import io.github.ppzxc.codec.exception.SerializeFailedException;
import io.github.ppzxc.codec.model.EncryptionMethod;
import io.github.ppzxc.codec.model.EncryptionMethodFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class ObjectOutputStreamMapperTest {

  private Mapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ObjectOutputStreamMapper();
  }

  @RepeatedTest(10)
  void should_serialize_when_no_type() throws SerializeFailedException {
    // given
    EncryptionMethod given = EncryptionMethodFixture.random();

    // when
    byte[] actual = mapper.write(given);

    // then
    assertThat(actual).isNotEmpty().hasSizeGreaterThanOrEqualTo(1);
  }

  @RepeatedTest(10)
  void should_serialize_when_type() throws SerializeFailedException {
    // given
    EncryptionMethod given = EncryptionMethodFixture.random();

    // when
    byte[] actual = mapper.write((byte) 0x01, given);

    // then
    assertThat(actual).isNotEmpty().hasSizeGreaterThanOrEqualTo(1);
  }

  @Test
  void should_throw_exception_when_input_write_null() {
    assertThatCode(() -> mapper.write(new Object()))
      .isInstanceOf(SerializeFailedException.class);
  }

  @RepeatedTest(10)
  void should_deserialize_when_no_type() throws DeserializeFailedException, SerializeFailedException {
    // given
    EncryptionMethod expected = EncryptionMethodFixture.random();
    byte[] given = mapper.write(expected);

    // when
    EncryptionMethod actual = mapper.read(given, EncryptionMethod.class);

    // then
    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @RepeatedTest(10)
  void should_deserialize_when_type() throws DeserializeFailedException, SerializeFailedException {
    // given
    EncryptionMethod expected = EncryptionMethodFixture.random();
    byte[] given = mapper.write(expected);

    // when
    EncryptionMethod actual = mapper.read((byte) 0x01, given, EncryptionMethod.class);

    // then
    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void should_throw_exception_when_input_read_null() {
    assertThatCode(() -> mapper.read(null, null))
      .isInstanceOf(DeserializeFailedException.class);
  }
}