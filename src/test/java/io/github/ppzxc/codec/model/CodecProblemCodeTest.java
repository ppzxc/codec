package io.github.ppzxc.codec.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class CodecProblemCodeTest {

  @ParameterizedTest
  @MethodSource("getAll")
  void should(CodecProblemCode expected) {
    assertThat(CodecProblemCode.of(expected.getCode())).isEqualByComparingTo(expected);
  }

  private static List<CodecProblemCode> getAll() {
    return Arrays.stream(CodecProblemCode.values())
      .collect(Collectors.toList());
  }
}