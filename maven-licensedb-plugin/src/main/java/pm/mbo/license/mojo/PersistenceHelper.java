package pm.mbo.license.mojo;

import org.apache.maven.project.MavenProject;
import pm.mbo.license.model.project.Module;
import pm.mbo.license.model.project.Project;
import pm.mbo.license.model.project.Version;
import pm.mbo.license.mojo.dal.project.ModuleRepository;
import pm.mbo.license.mojo.dal.project.ProjectRepository;
import pm.mbo.license.mojo.dal.project.VersionRepository;
import pm.mbo.license.mojo.dal.project.query.FindModuleByVersionAndMavenCoordinatesQuery;
import pm.mbo.license.mojo.dal.project.query.FindProjectByNameQuery;
import pm.mbo.license.mojo.dal.project.query.FindVersionByProjectAndNameQuery;
import pm.mbo.license.mojo.metadata.ProjectMetadata;

import java.util.Calendar;

public final class PersistenceHelper {

    protected static Module persistProjectStructure(final ProjectRepository projectRepository,
                                                    final VersionRepository versionRepository,
                                                    final ModuleRepository moduleRepository,
                                                    final ProjectMetadata projectMetadata,
                                                    final MavenProject mavenProject) {
        final Project project = persistProject(projectMetadata, projectRepository);
        final Version version = persistVersion(versionRepository, mavenProject, project);
        return persistModule(moduleRepository, mavenProject, version);
    }

    private static Module persistModule(final ModuleRepository moduleRepository, final MavenProject mavenProject, final Version version) {
        final Module module = new Module();
        module.setVersion(version);
        module.setMavenGroupId(mavenProject.getGroupId());
        module.setMavenArtifactId(mavenProject.getArtifactId());
        module.setMavenPackaging(mavenProject.getPackaging());
        module.setFullCoordinates(module.createFullCoordinates());

        return moduleRepository.findOrCreate(new FindModuleByVersionAndMavenCoordinatesQuery(module));
    }

    private static Version persistVersion(final VersionRepository versionRepository, final MavenProject mavenProject, final Project project) {
        Version version = new Version();
        version.setProject(project);
        version.setName(mavenProject.getVersion());

        return versionRepository.findOrCreate(new FindVersionByProjectAndNameQuery(version));
    }

    private static Project persistProject(final ProjectMetadata projectMetadata, final ProjectRepository projectRepository) {
        Project project = new Project();
        project.setKey(projectMetadata.getProjectId());
        project.setName(projectMetadata.getProjectName());
        project.setComponent(projectMetadata.getProjectComponent());
        project.setLastBuild(Calendar.getInstance());

        project = projectRepository.findOrCreate(new FindProjectByNameQuery(project));
        project.setName(projectMetadata.getProjectName());
        project.setComponent(projectMetadata.getProjectComponent());
        project.setLastBuild(Calendar.getInstance());
        return projectRepository.merge(project);
    }

    private PersistenceHelper() {
        throw new IllegalAccessError();
    }

}
