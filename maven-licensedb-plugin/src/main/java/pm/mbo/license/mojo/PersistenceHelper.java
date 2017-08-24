package pm.mbo.license.mojo;

import org.apache.maven.project.MavenProject;
import pm.mbo.license.model.artifact.Artifact;
import pm.mbo.license.model.project.ArtifactModuleMapping;
import pm.mbo.license.model.project.Module;
import pm.mbo.license.model.project.Project;
import pm.mbo.license.model.project.Version;
import pm.mbo.license.model.variation.ArtifactLicenseVariationMapping;
import pm.mbo.license.model.variation.LicenseVariation;
import pm.mbo.license.mojo.dal.Repository;
import pm.mbo.license.mojo.dal.artifact.query.FindArtifactByMavenCoordinatesQuery;
import pm.mbo.license.mojo.dal.project.query.FindArtifactModuleMappingByForeignKeysQuery;
import pm.mbo.license.mojo.dal.project.query.FindModuleByVersionAndMavenCoordinatesQuery;
import pm.mbo.license.mojo.dal.project.query.FindProjectByNameQuery;
import pm.mbo.license.mojo.dal.project.query.FindVersionByProjectAndNameQuery;
import pm.mbo.license.mojo.dal.variation.query.FindArtifactLicenseVariationMappingByForeignKeysQuery;
import pm.mbo.license.mojo.dal.variation.query.FindLicenseVariationByNameQuery;
import pm.mbo.license.mojo.metadata.ArtifactMetadata;
import pm.mbo.license.mojo.metadata.ProjectMetadata;

import java.util.Calendar;

public class PersistenceHelper {

    protected static Module persistProjectStructure(final Repository<Long, Project> projectRepository,
                                                    final Repository<Long, Version> versionRepository,
                                                    final Repository<Long, Module> moduleRepository,
                                                    final ProjectMetadata projectMetadata,
                                                    final MavenProject mavenProject) {
        final Project project = persistProject(projectMetadata, projectRepository);
        final Version version = persistVersion(versionRepository, mavenProject, project);
        return persistModule(moduleRepository, mavenProject, version);
    }

    protected static Module persistModule(final Repository<Long, Module> moduleRepository, final MavenProject mavenProject, final Version version) {
        final Module module = new Module();
        module.setVersion(version);
        module.setMavenGroupId(mavenProject.getGroupId());
        module.setMavenArtifactId(mavenProject.getArtifactId());
        module.setMavenPackaging(mavenProject.getPackaging());
        module.setFullCoordinates(module.createFullCoordinates());

        return moduleRepository.findOrCreate(new FindModuleByVersionAndMavenCoordinatesQuery(module));
    }

    protected static Version persistVersion(final Repository<Long, Version> versionRepository, final MavenProject mavenProject, final Project project) {
        Version version = new Version();
        version.setProject(project);
        version.setName(mavenProject.getVersion());

        return versionRepository.findOrCreate(new FindVersionByProjectAndNameQuery(version));
    }

    protected static Project persistProject(final ProjectMetadata projectMetadata, final Repository<Long, Project> projectRepository) {
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

    protected static Artifact persistDependency(final Repository<Long, Artifact> artifactRepository,
                                                final MavenProject mavenProject,
                                                final String coordinates,
                                                final ArtifactMetadata metadata) {

        Artifact artifact = new Artifact();

        artifact.setMavenGroupId(mavenProject.getGroupId());
        artifact.setMavenArtifactId(mavenProject.getArtifactId());
        artifact.setMavenVersion(mavenProject.getVersion());

        artifact.setMavenCoordinates(coordinates);

        artifact.setMavenScope(metadata.getScope());
        artifact.setMavenType(metadata.getType());

        return artifactRepository.findOrCreate(new FindArtifactByMavenCoordinatesQuery(artifact));
    }

    protected static void persistArtifactModuleMapping(final Repository<Long, ArtifactModuleMapping> artifactModuleMappingRepository,
                                                final Artifact artifact,
                                                final Module module) {
        final ArtifactModuleMapping artifactModuleMapping = new ArtifactModuleMapping();
        artifactModuleMapping.setModule(module);
        artifactModuleMapping.setArtifact(artifact);
        artifactModuleMappingRepository.findOrCreate(new FindArtifactModuleMappingByForeignKeysQuery(artifactModuleMapping));
    }

    protected static void persistArtifactLicenseVariationMapping(final Repository<Long, ArtifactLicenseVariationMapping> variationMappingRepository,
                                                          final Artifact artifact,
                                                          final LicenseVariation licenseVariation) {
        final ArtifactLicenseVariationMapping artifactLicenseVariationMapping = new ArtifactLicenseVariationMapping();
        artifactLicenseVariationMapping.setArtifact(artifact);
        artifactLicenseVariationMapping.setLicenseVariation(licenseVariation);
        variationMappingRepository.findOrCreate(
                new FindArtifactLicenseVariationMappingByForeignKeysQuery(artifactLicenseVariationMapping));
    }

    protected static LicenseVariation persistLicenseVariation(final Repository<Long, LicenseVariation> licenseVariationRepository,
                                                       final String licenseString) {
        final LicenseVariation variation = new LicenseVariation();
        variation.setName(licenseString);

        return licenseVariationRepository.findOrCreate(new FindLicenseVariationByNameQuery(variation));
    }
}
