package org.gradle;

import org.gradle.bootstrap.HostApplication;

/**
 * TODO when startApplication fails the System doesn't exit...
 *
 * @author Tom Eyckmans
 */
public class OsgiMain extends AbstractMain {

    private final HostApplication app = new HostApplication();

    public OsgiMain(String[] args) {
        super(args);
        setBuildCompleter(new BuildCompleter() {
            @Override
            public void exit(Throwable failure) {
                try {
                    app.stopApplication();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        });
    }

    public static void main(String[] args) throws Throwable {
        new OsgiMain(args).execute();
    }

    @Override
    protected GradleFactory getGradleFactory() {
        if ( gradleFactory == null ) {
            try {
                app.startApplication();
            }
            catch ( Exception e ) {
                throw new RuntimeException(e);
            }

            gradleFactory = app.getGradleFactory();
        }
        return gradleFactory;
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
