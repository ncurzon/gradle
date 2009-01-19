package org.gradle;

import org.gradle.api.GradleException;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Test;

public class BuildResultTest {
    @Test
    public void rethrowDoesNothingWhenNoBuildFailure() {
        BuildResult result = new DefaultBuildResult(null, null);
        result.rethrowFailure();
    }

    @Test
    public void rethrowsGradleException() {
        Throwable failure = new GradleException();
        BuildResult result = new DefaultBuildResult(null, failure);

        try {
            result.rethrowFailure();
            fail();
        } catch (Exception e) {
            assertThat(e, sameInstance(failure));
        }
    }

    @Test
    public void rethrowWrapsOtherExceptions() {
        Throwable failure = new RuntimeException();
        BuildResult result = new DefaultBuildResult(null, failure);

        try {
            result.rethrowFailure();
            fail();
        } catch (GradleException e) {
            assertThat(e.getMessage(), equalTo("Build aborted because of an internal error."));
            assertThat(e.getCause(), sameInstance(failure));
        }
    }
}
