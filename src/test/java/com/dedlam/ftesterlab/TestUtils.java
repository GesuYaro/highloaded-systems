package com.dedlam.ftesterlab;

import java.util.function.Consumer;

public class TestUtils {
  public static <T> T create(T instance, Consumer<T> builder) {
    builder.accept(instance);
    return instance;
  }
}
