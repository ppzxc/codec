//package io.github.ppzxc.codec.mapper;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatCode;
//
//import io.github.ppzxc.codec.exception.DeserializeFailedException;
//import io.github.ppzxc.codec.exception.SerializeFailedException;
//import io.github.ppzxc.codec.model.EncodingType;
//import io.github.ppzxc.codec.model.EncryptionMethod;
//import io.github.ppzxc.codec.model.EncryptionMethodFixture;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.RepeatedTest;
//
//class AbstractMultiMapperTest {
//
//  private MultiMapper multiMapper;
//
//  @BeforeEach
//  void setUp() {
//    multiMapper = new AbstractMultiMapper();
//  }
//
//  @RepeatedTest(10)
//  void should_serialize_when_no_type() throws SerializeFailedException {
//    // given
//    EncryptionMethod given = EncryptionMethodFixture.random();
//
//    // when
//    byte[] actual = multiMapper.write(WriteCommand.of(EncodingType.JAVA_BINARY, given));
//
//    // then
//    assertThat(actual).isNotEmpty().hasSizeGreaterThanOrEqualTo(1);
//  }
//
//  @RepeatedTest(10)
//  void should_serialize_when_type() throws SerializeFailedException {
//    // given
//    EncryptionMethod given = EncryptionMethodFixture.random();
//
//    // when
//    byte[] actual = multiMapper.write(WriteCommand.of(EncodingType.JAVA_BINARY, given));
//
//    // then
//    assertThat(actual).isNotEmpty().hasSizeGreaterThanOrEqualTo(1);
//  }
//
//  @RepeatedTest(10)
//  void should_throw_exception_when_input_write_null() {
//    assertThatCode(() -> multiMapper.write(WriteCommand.of(EncodingType.JAVA_BINARY, new Object())))
//      .isInstanceOf(SerializeFailedException.class);
//  }
//
//  @RepeatedTest(10)
//  void should_deserialize_when_no_type() throws DeserializeFailedException, SerializeFailedException {
//    // given
//    EncryptionMethod expected = EncryptionMethodFixture.random();
//    byte[] given = multiMapper.write(WriteCommand.of(EncodingType.JAVA_BINARY, expected));
//
//    // when
//    EncryptionMethod actual = multiMapper.read(ReadCommand.of(EncodingType.JAVA_BINARY, given, EncryptionMethod.class));
//
//    // then
//    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
//  }
//
//  @RepeatedTest(10)
//  void should_deserialize_when_type() throws DeserializeFailedException, SerializeFailedException {
//    // given
//    EncryptionMethod expected = EncryptionMethodFixture.random();
//    byte[] given = multiMapper.write(WriteCommand.of(EncodingType.JAVA_BINARY, expected));
//
//    // when
//    EncryptionMethod actual = multiMapper.read(ReadCommand.of(EncodingType.JAVA_BINARY, given, EncryptionMethod.class));
//
//    // then
//    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
//  }
//
//  @RepeatedTest(10)
//  void should_throw_exception_when_input_read_null() {
//    assertThatCode(() -> multiMapper.read(ReadCommand.of(null, null, null)))
//      .isInstanceOf(DeserializeFailedException.class);
//  }
//}