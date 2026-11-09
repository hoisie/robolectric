package org.robolectric;

import static com.google.common.truth.Truth.assertThat;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.robolectric.annotation.Config;

/** Checks how many times the {@code @Parameters} method of a parameterized test is called. */
@RunWith(JUnit4.class)
public final class ParameterizedRobolectricTestRunnerParametersCallsTest {
  // The test class runs in sandboxes with their own class loaders, so the count is kept in a system
  // property, which all class loaders share.
  private static final String CALLS = "ParameterizedRobolectricTestRunnerParametersCallsTest.calls";

  @Test
  public void parametersAreComputedOnceForDiscoveryAndOncePerSandbox() throws Throwable {
    System.clearProperty(CALLS);
    try {
      Result result =
          new JUnitCore().run(new ParameterizedRobolectricTestRunner(ParameterizedTest.class));

      assertThat(result.getFailures()).isEmpty();
      assertThat(result.getRunCount()).isEqualTo(12);
      // Once to discover the tests, then once in each of the two sandboxes.
      assertThat(Integer.getInteger(CALLS)).isEqualTo(3);
    } finally {
      System.clearProperty(CALLS);
    }
  }

  @Config(sdk = {Config.OLDEST_SDK, Config.NEWEST_SDK})
  public static final class ParameterizedTest {
    private final int value;

    public ParameterizedTest(int value) {
      this.value = value;
    }

    @ParameterizedRobolectricTestRunner.Parameters
    public static Collection<?> parameters() {
      System.setProperty(CALLS, String.valueOf(Integer.getInteger(CALLS, 0) + 1));
      return Arrays.asList(new Object[][] {{1}, {2}, {3}});
    }

    @Test
    public void first() {
      assertThat(value).isAtLeast(1);
    }

    @Test
    public void second() {
      assertThat(value).isAtMost(3);
    }
  }
}
