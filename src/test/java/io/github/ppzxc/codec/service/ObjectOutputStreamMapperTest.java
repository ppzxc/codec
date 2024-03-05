package io.github.ppzxc.codec.service;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ppzxc.codec.model.EncryptionMethodFixture;
import io.github.ppzxc.codec.model.EncryptionMethod;
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
  void should_serialize() {
    // given
    EncryptionMethod given = EncryptionMethodFixture.random();

    // when
    byte[] actual = mapper.write(given);

    // then
    assertThat(actual).isNotEmpty().hasSizeGreaterThanOrEqualTo(1);
  }

  @RepeatedTest(10)
  void should_deserialize() {
    // given
    EncryptionMethod expected = EncryptionMethodFixture.random();
    byte[] given = mapper.write(expected);

    // when
    EncryptionMethod actual = mapper.read(given, EncryptionMethod.class);

    // then
    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }
}