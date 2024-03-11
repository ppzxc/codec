package io.github.ppzxc.codec.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.github.ppzxc.codec.exception.DeserializeFailedException;
import io.github.ppzxc.codec.exception.SerializeFailedException;
import io.github.ppzxc.codec.model.EncodingType;
import io.github.ppzxc.codec.model.EncryptionMethod;
import io.github.ppzxc.codec.model.EncryptionMethodProtobufFixture;
import io.github.ppzxc.codec.model.TestUser;
import io.github.ppzxc.codec.model.protobuf.EncryptionMethodProtobuf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultMultiMapperTest {

  private MultiMapper multiMapper;

  @BeforeEach
  void setUp() {
    multiMapper = DefaultMultiMapper.create(new ProtoObjectMapper());
  }

  @Test
  void should_serialize_when_json_command() throws SerializeFailedException {
    // given
    TestUser given = TestUser.random();

    // when
    byte[] actual = multiMapper.write(WriteCommand.of(EncodingType.JSON, given));

    // then
    assertThat(actual).isNotEmpty().hasSizeGreaterThan(0);
  }

  @Test
  void should_deserialize_when_json_command() throws SerializeFailedException, DeserializeFailedException {
    // given
    TestUser expected = TestUser.random();
    byte[] given = multiMapper.write(WriteCommand.of(EncodingType.JSON, expected));

    // when
    TestUser actual = multiMapper.read(ReadCommand.of(EncodingType.JSON, given, TestUser.class));

    // then
    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void should_serialize_when_bson_command() throws SerializeFailedException {
    // given
    TestUser given = TestUser.random();

    // when
    byte[] actual = multiMapper.write(WriteCommand.of(EncodingType.BSON, given));

    // then
    assertThat(actual).isNotEmpty().hasSizeGreaterThan(0);
  }

  @Test
  void should_deserialize_when_bson_command() throws SerializeFailedException, DeserializeFailedException {
    // given
    TestUser expected = TestUser.random();
    byte[] given = multiMapper.write(WriteCommand.of(EncodingType.BSON, expected));

    // when
    TestUser actual = multiMapper.read(ReadCommand.of(EncodingType.BSON, given, TestUser.class));

    // then
    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void should_serialize_when_java_command() throws SerializeFailedException {
    // given
    TestUser given = TestUser.random();

    // when
    byte[] actual = multiMapper.write(WriteCommand.of(EncodingType.JAVA_BINARY, given));

    // then
    assertThat(actual).isNotEmpty().hasSizeGreaterThan(0);
  }

  @Test
  void should_deserialize_when_java_command() throws SerializeFailedException, DeserializeFailedException {
    // given
    TestUser expected = TestUser.random();
    byte[] given = multiMapper.write(WriteCommand.of(EncodingType.JAVA_BINARY, expected));

    // when
    TestUser actual = multiMapper.read(ReadCommand.of(EncodingType.JAVA_BINARY, given, TestUser.class));

    // then
    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void should_throw_exception_when_null_proto_mapper_1() {
    assertThatCode(() -> DefaultMultiMapper.create().write(WriteCommand.of(EncodingType.PROTOBUF, new Object())))
      .isInstanceOf(SerializeFailedException.class);
  }

  @Test
  void should_throw_exception_when_null_proto_mapper_2() {
    assertThatCode(
      () -> DefaultMultiMapper.create().read(ReadCommand.of(EncodingType.PROTOBUF, new byte[0], Object.class)))
      .isInstanceOf(DeserializeFailedException.class);
  }

  @Test
  void should_throw_exception_when_null_proto_mapper_3() {
    assertThatCode(
      () -> DefaultMultiMapper.create().read(ReadCommand.of(EncodingType.NULL, new byte[0], Object.class)))
      .isInstanceOf(DeserializeFailedException.class);
  }

  @Test
  void should_serialize_when_proto_command() throws SerializeFailedException {
    WriteCommand command = WriteCommand.of(EncodingType.PROTOBUF, TestUser.random());
    assertThat(command.getType()).isEqualByComparingTo(EncodingType.PROTOBUF);
    assertThat(multiMapper.write(command)).isEmpty();
  }

  @Test
  void should_serialize_when_proto_command_2() {
    assertThatCode(() -> multiMapper.write(WriteCommand.of(EncodingType.NULL, TestUser.random())))
      .isInstanceOf(SerializeFailedException.class);
  }

  @Test
  void should_deserialize_when_proto_command() throws DeserializeFailedException {
    // given
    EncryptionMethodProtobuf expected = EncryptionMethodProtobufFixture.random();

    // when
    EncryptionMethod actual = multiMapper.read(
      ReadCommand.of(EncodingType.PROTOBUF, expected.toByteArray(), EncryptionMethod.class));

    // then
    assertThat(actual.getType().name()).isEqualToIgnoringCase(expected.getType().name());
    assertThat(actual.getMode().name()).isEqualToIgnoringCase(expected.getMode().name());
    assertThat(actual.getPadding().name()).isEqualToIgnoringCase(expected.getPadding().name());
    assertThat(actual.getIv()).isEqualTo(expected.getIv());
    assertThat(actual.getSymmetricKey()).isEqualTo(expected.getSymmetricKey());
  }
}