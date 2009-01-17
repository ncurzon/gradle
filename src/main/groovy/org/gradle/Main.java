package org.gradle;

import org.gradle.initialization.DefaultLoggingConfigurer;

/**
 * @author Tom Eyckmans
 */
public class Main extends AbstractMain {

    public Main(String[] args) {
        super(args);
        super.gradleFactory = new DefaultGradleFactory(new DefaultLoggingConfigurer());
    }

    @Override
    protected GradleFactory getGradleFactory() {
        return gradleFactory;
    }

    public static void main(String[] args) throws Throwable {
        new Main(args).execute();
    }

    public void setGradleFactory(GradleFactory gradleFactory) {
        this.gradleFactory = gradleFactory;
    }

    @Override
    protected void doWithStartParameter(StartParameter startParameter) {
        try {
            final Gradle gradle = gradleFactory.newInstance(startParameter);

            gradle.addBuildListener(exceptionReporter);
            gradle.addBuildListener(resultLogger);

            final BuildResult buildResult = gradle.run();
            if (buildResult.getFailure() != null) {
                buildCompleter.exit(buildResult.getFailure());
            }
        } catch (Throwable e) {
            exceptionReporter.buildFinished(new DefaultBuildResult(null, e));
            buildCompleter.exit(e);
        }
        buildCompleter.exit(null);
    }


}
