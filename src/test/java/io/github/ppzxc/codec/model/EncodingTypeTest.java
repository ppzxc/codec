package io.github.ppzxc.codec.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class EncodingTypeTest {

  @ParameterizedTest
  @MethodSource("getAll")
  void should(EncodingType expected) {
    assertThat(EncodingType.of(expected.getCode())).isEqualByComparingTo(expected);
  }

  private static List<EncodingType> getAll() {
    return Arrays.stream(EncodingType.values())
      .collect(Collectors.toList());
  }
}