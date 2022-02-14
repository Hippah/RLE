package me.hippo.api.testing.rle.topic;

import java.io.Serializable;

/**
 * @author Hippo
 * @version 1.0.1, 4/21/20
 * @since 1.0.0
 */
public final class ExampleTopic implements Serializable {

  private final int test;

  public ExampleTopic(int test) {
    this.test = test;
  }

  public int getTest() {
    return test;
  }

  @Override
  public String toString() {
    return "ExampleTopic{" +
        "test=" + test +
        '}';
  }
}
