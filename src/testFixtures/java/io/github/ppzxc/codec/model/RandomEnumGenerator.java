package io.github.ppzxc.codec.model;

import java.util.concurrent.ThreadLocalRandom;

public class RandomEnumGenerator<T extends Enum<T>> {

  private final T[] values;

  public RandomEnumGenerator(Class<T> e) {
    values = e.getEnumConstants();
  }

  public T randomEnum() {
    return values[ThreadLocalRandom.current().nextInt(values.length)];
  }
}
