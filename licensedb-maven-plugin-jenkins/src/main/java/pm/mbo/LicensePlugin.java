package pm.mbo;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Proc;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.tasks.Maven;
import jenkins.tasks.SimpleBuildStep;
import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.bind.JavaScriptMethod;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.UUID;

public class LicensePlugin extends Builder implements SimpleBuildStep {

    @Getter
    private final String projectId;
    @Getter
    private final String name;
    @Getter
    private final String component;
    @Getter
    private final Integer timeout;
    @Getter
    private final Boolean dryRun;
    @Getter
    private final Boolean skip;

    @DataBoundConstructor
    public LicensePlugin(final String projectId,
                         final String name,
                         final String component,
                         final Integer timeout,
                         final boolean dryRun,
                         final boolean skip) {
        this.projectId = projectId;
        this.name = name;
        this.component = component;
        this.timeout = timeout;
        this.dryRun = dryRun;
        this.skip = skip;
    }

    @Override
    public void perform(@Nonnull final Run<?, ?> build,
                        @Nonnull final FilePath workspace,
                        @Nonnull final Launcher launcher,
                        @Nonnull final TaskListener listener) throws InterruptedException, IOException {
        try {
            final Launcher.ProcStarter starter = launcher.launch();
            starter.cmds(new CommandBuilder().createCommandFrom(this));
            starter.stdout(listener);
            starter.envs(build.getEnvironment(listener));
            starter.pwd(workspace);

            final Proc proc = launcher.launch(starter);
            int exitCode = proc.join();
            listener.getLogger().println("command exited with code " + exitCode);
        } catch (IOException | InterruptedException e) {
            listener.error(e.getMessage());
        }
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        @Getter
        @Setter
        private String mavenBin = "$MAVEN_HOME/bin/mvn";
        @Getter
        @Setter
        private String mavenGoal = "-U pm.mbo:licensedb-maven-plugin:add-third-party-database";
        @Getter
        @Setter
        private Integer timeout = 120;
        @Getter
        @Setter
        private String dbUser = "license";
        @Getter
        @Setter
        private String dbPass = "license";
        @Getter
        @Setter
        private String dbUrl = "jdbc:postgresql://localhost:5432/license";
        @Getter
        @Setter
        private String dbGeneration = "update";
        @Getter
        @Setter
        private String dbDialect = "org.hibernate.dialect.PostgreSQL94Dialect";
        @Getter
        @Setter
        private String dbDriver = "org.postgresql.Driver";
        @Getter
        @Setter
        private Boolean dryRun = false;
        @Getter
        @Setter
        private Boolean skip = false;

        public DescriptorImpl() {
            load();
        }

        @Override
        public boolean isApplicable(final Class<? extends AbstractProject> jobType) {
            return Maven.ProjectWithMaven.class.isAssignableFrom(jobType);
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return "License Plugin";
        }

        @Override
        public boolean configure(final StaplerRequest req, final JSONObject formData) throws FormException {
            req.bindJSON(this, formData);
            // ^Can also use req.bindJSON(this, formData);
            //  (easier when there are many fields; need set* methods for this, like setUseFrench)
            save();
            return super.configure(req, formData);
        }

        @JavaScriptMethod
        public synchronized String createUUID() {
            return UUID.randomUUID().toString();
        }

    }
}
