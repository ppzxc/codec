package io.github.ppzxc.codec.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class CodecCodeTest {

  @ParameterizedTest
  @MethodSource("getAll")
  void should(CodecCode expected) {
    assertThat(CodecCode.of(expected.getCode())).isEqualByComparingTo(expected);
  }

  private static List<CodecCode> getAll() {
    return Arrays.stream(CodecCode.values())
      .collect(Collectors.toList());
  }
}