package pm.mbo.license.mojo.dal;

import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;
import pm.mbo.license.model.artifact.Artifact;
import pm.mbo.license.model.project.ArtifactModuleMapping;
import pm.mbo.license.model.project.Module;
import pm.mbo.license.model.project.Project;
import pm.mbo.license.model.project.Version;
import pm.mbo.license.model.variation.ArtifactLicenseVariationMapping;
import pm.mbo.license.model.variation.LicenseVariation;
import pm.mbo.license.mojo.dal.artifact.query.FindArtifactByMavenCoordinatesQuery;
import pm.mbo.license.mojo.dal.project.query.FindArtifactModuleMappingByForeignKeysQuery;
import pm.mbo.license.mojo.dal.project.query.FindModuleByVersionAndMavenCoordinatesQuery;
import pm.mbo.license.mojo.dal.project.query.FindProjectByNameQuery;
import pm.mbo.license.mojo.dal.project.query.FindVersionByProjectAndNameQuery;
import pm.mbo.license.mojo.dal.variation.query.FindArtifactLicenseVariationMappingByForeignKeysQuery;
import pm.mbo.license.mojo.dal.variation.query.FindLicenseVariationByNameQuery;
import pm.mbo.license.mojo.helper.Conditions;
import pm.mbo.license.mojo.metadata.ArtifactMetadata;
import pm.mbo.license.mojo.metadata.ProjectMetadata;

import java.util.Calendar;

@Component(role = PersistenceHelper.class, hint = "default")
public class PersistenceHelper {

    private Repository<Long, Project> projectRepository;
    private Repository<Long, Version> versionRepository;
    private Repository<Long, Module> moduleRepository;

    private Repository<Long, pm.mbo.license.model.artifact.Artifact> artifactRepository;
    private Repository<Long, ArtifactModuleMapping> artifactModuleMappingRepository;

    private Repository<Long, LicenseVariation> licenseVariationRepository;
    private Repository<Long, ArtifactLicenseVariationMapping> variationMappingRepository;

    private boolean needsInit = true;

    public void initRepos(final EntityManagerDelegate emd) {
        Conditions.notNull("emd", emd);
        projectRepository = new Repository<>(emd);
        versionRepository = new Repository<>(emd);
        moduleRepository = new Repository<>(emd);

        artifactRepository = new Repository<>(emd);
        artifactModuleMappingRepository = new Repository<>(emd);

        licenseVariationRepository = new Repository<>(emd);
        variationMappingRepository = new Repository<>(emd);
        needsInit = false;
    }

    public Module persistProjectStructure(final ProjectMetadata projectMetadata, final MavenProject mavenProject) {
        checkInit();
        final Project project = persistProject(projectMetadata);
        final Version version = persistVersion(mavenProject, project);
        return persistModule(mavenProject, version);
    }

    public Module persistModule(final MavenProject mavenProject, final Version version) {
        checkInit();
        final Module module = new Module();
        module.setVersion(version);
        module.setMavenGroupId(mavenProject.getGroupId());
        module.setMavenArtifactId(mavenProject.getArtifactId());
        module.setMavenPackaging(mavenProject.getPackaging());
        module.setFullCoordinates(module.createFullCoordinates());

        return moduleRepository.findOrCreate(new FindModuleByVersionAndMavenCoordinatesQuery(module));
    }

    public Version persistVersion(final MavenProject mavenProject, final Project project) {
        checkInit();
        Version version = new Version();
        version.setProject(project);
        version.setName(mavenProject.getVersion());

        return versionRepository.findOrCreate(new FindVersionByProjectAndNameQuery(version));
    }

    public Project persistProject(final ProjectMetadata projectMetadata) {
        checkInit();
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

    public Artifact persistDependency(final MavenProject mavenProject,
                                      final String coordinates,
                                      final ArtifactMetadata metadata) {
        checkInit();
        Artifact artifact = new Artifact();

        artifact.setMavenGroupId(mavenProject.getGroupId());
        artifact.setMavenArtifactId(mavenProject.getArtifactId());
        artifact.setMavenVersion(mavenProject.getVersion());

        artifact.setMavenCoordinates(coordinates);

        artifact.setMavenScope(metadata.getScope());
        artifact.setMavenType(metadata.getType());

        return artifactRepository.findOrCreate(new FindArtifactByMavenCoordinatesQuery(artifact));
    }

    public void persistArtifactModuleMapping(final Artifact artifact, final Module module) {
        checkInit();
        final ArtifactModuleMapping artifactModuleMapping = new ArtifactModuleMapping();
        artifactModuleMapping.setModule(module);
        artifactModuleMapping.setArtifact(artifact);
        artifactModuleMappingRepository.findOrCreate(new FindArtifactModuleMappingByForeignKeysQuery(artifactModuleMapping));
    }

    public void persistArtifactLicenseVariationMapping(final Artifact artifact, final LicenseVariation licenseVariation) {
        checkInit();
        final ArtifactLicenseVariationMapping artifactLicenseVariationMapping = new ArtifactLicenseVariationMapping();
        artifactLicenseVariationMapping.setArtifact(artifact);
        artifactLicenseVariationMapping.setLicenseVariation(licenseVariation);
        variationMappingRepository.findOrCreate(
                new FindArtifactLicenseVariationMappingByForeignKeysQuery(artifactLicenseVariationMapping));
    }

    public LicenseVariation persistLicenseVariation(final String licenseString) {
        checkInit();
        final LicenseVariation variation = new LicenseVariation();
        variation.setName(licenseString);

        return licenseVariationRepository.findOrCreate(new FindLicenseVariationByNameQuery(variation));
    }

    protected void checkInit() {
        if (needsInit) {
            throw new IllegalStateException("call initRepos before calling other methods");
        }
    }
}
