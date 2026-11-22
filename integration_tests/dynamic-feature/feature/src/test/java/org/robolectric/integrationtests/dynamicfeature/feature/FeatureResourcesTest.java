package org.robolectric.integrationtests.dynamicfeature.feature;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

/** Robolectric tests that run inside a dynamic feature module. */
@RunWith(RobolectricTestRunner.class)
public class FeatureResourcesTest {
  @Test
  public void getString_featureResource() {
    assertThat(RuntimeEnvironment.getApplication().getString(R.string.feature_greeting))
        .isEqualTo("Hello from the feature");
  }
}
