package pm.mbo.license.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;
import pm.mbo.license.mojo.helper.MavenHelper;
import pm.mbo.license.mojo.metadata.ProjectMetadata;

import java.util.List;

@Mojo(name = "aggregate-add-third-party-database", aggregator = true, defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
@Execute(goal = "add-third-party-database")
public class AggregatorAddThirdPartyDatabaseMojo extends AbstractMojo {

    @Parameter(property = "licensedb.projectId", required = true)
    private String projectId;

    @Parameter(property = "licensedb.projectName", required = true)
    private String projectName;

    @Parameter(property = "licensedb.projectComponent")
    private String projectComponent;

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Parameter(property = "reactorProjects", readonly = true, required = true)
    private List<?> reactorProjects;

    @Component
    private MavenHelper mavenHelper;

    private final Log log = getLog();

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final ProjectMetadata projectMetadata = new ProjectMetadata(projectId, projectName, projectComponent);
        log.info("aggregator for " + projectMetadata);
        if (null != reactorProjects) {
            for (Object project : reactorProjects) {
                final MavenProject mavenProject = (MavenProject) project;
                if (mavenProject.getPackaging().equalsIgnoreCase("pom")) {
                    continue;
                }
                log.info("project: " + mavenHelper.getMavenProjectCoordinates(mavenProject));
            }

        }
    }
}
