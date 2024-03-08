package io.github.ppzxc.codec.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.github.ppzxc.codec.exception.DeserializeFailedException;
import io.github.ppzxc.codec.exception.SerializeFailedException;
import io.github.ppzxc.codec.model.EncryptionMethod;
import io.github.ppzxc.codec.model.EncryptionMethodFixture;
import org.junit.jupiter.api.Test;

class BsonObjectMapperTest {

  @Test
  void should_serialize_when_EncryptionMethod() {
    assertThatCode(() -> BsonObjectMapper.create().write(EncryptionMethodFixture.random()))
      .doesNotThrowAnyException();
  }

  @Test
  void should_deserialize_when_EncryptionMethod() throws SerializeFailedException, DeserializeFailedException {
    // given
    EncryptionMethod given = EncryptionMethodFixture.random();
    BsonObjectMapper bsonObjectMapper = BsonObjectMapper.create();

    // when, then
    assertThat(bsonObjectMapper.read(bsonObjectMapper.write(given), EncryptionMethod.class))
      .usingRecursiveComparison()
      .isEqualTo(given);
  }
}