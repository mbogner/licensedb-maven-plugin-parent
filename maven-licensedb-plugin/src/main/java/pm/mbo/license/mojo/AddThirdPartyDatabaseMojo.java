package pm.mbo.license.mojo;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.license.AddThirdPartyMojo;
import org.codehaus.mojo.license.model.LicenseMap;
import pm.mbo.license.model.project.ArtifactModuleMapping;
import pm.mbo.license.model.project.Module;
import pm.mbo.license.model.variation.ArtifactLicenseVariationMapping;
import pm.mbo.license.model.variation.LicenseVariation;
import pm.mbo.license.mojo.dal.EntityManagerDelegate;
import pm.mbo.license.mojo.dal.artifact.ArtifactRepository;
import pm.mbo.license.mojo.dal.artifact.query.FindArtifactByMavenCoordinatesQuery;
import pm.mbo.license.mojo.dal.project.ArtifactModuleMappingRepository;
import pm.mbo.license.mojo.dal.project.ModuleRepository;
import pm.mbo.license.mojo.dal.project.ProjectRepository;
import pm.mbo.license.mojo.dal.project.VersionRepository;
import pm.mbo.license.mojo.dal.project.query.FindArtifactModuleMappingByForeignKeysQuery;
import pm.mbo.license.mojo.dal.variation.ArtifactLicenseVariationMappingRepository;
import pm.mbo.license.mojo.dal.variation.LicenseVariationRepository;
import pm.mbo.license.mojo.dal.variation.query.FindArtifactLicenseVariationMappingByForeignKeysQuery;
import pm.mbo.license.mojo.dal.variation.query.FindLicenseVariationByNameQuery;
import pm.mbo.license.mojo.metadata.ArtifactMetadata;
import pm.mbo.license.mojo.metadata.ProjectMetadata;

import java.util.Map;
import java.util.Set;

@Mojo(name = "add-third-party-database", requiresDependencyResolution = ResolutionScope.TEST,
        defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class AddThirdPartyDatabaseMojo extends AddThirdPartyMojo {

    @Parameter(property = "licensedb.projectId", required = true)
    private String projectId;

    @Parameter(property = "licensedb.projectName", required = true)
    private String projectName;

    @Parameter(property = "licensedb.projectComponent")
    private String projectComponent;

    @Parameter(property = "licensedb.databaseUrl", defaultValue = "jdbc:h2:file:~/license")
    private String databaseUrl;

    @Parameter(property = "licensedb.databaseUser", defaultValue = "sa")
    private String databaseUser;

    @Parameter(property = "licensedb.databasePassword", defaultValue = "")
    private String databasePassword;

    @Parameter(property = "licensedb.databaseDriverClass", defaultValue = "org.h2.Driver")
    private String databaseDriverClass;

    @Parameter(property = "licensedb.databaseDialect", defaultValue = "org.hibernate.dialect.H2Dialect")
    private String databaseDialect;

    @Parameter(property = "licensedb.databaseHbm2ddl", defaultValue = "update")
    private String databaseHbm2ddl;

    @Parameter(property = "licensedb.debug", defaultValue = "false")
    private boolean debug;

    @Parameter(property = "licensedb.dryRun", defaultValue = "false")
    private boolean dryRun;

    @Parameter(property = "licensedb.skip", defaultValue = "false")
    private boolean skip;

    @Parameter(property = "project.artifacts", required = true, readonly = true)
    private Set<Artifact> dependencies;

    private final Log log = getLog();

    private ProjectRepository projectRepository;
    private VersionRepository versionRepository;
    private ModuleRepository moduleRepository;

    private ArtifactRepository artifactRepository;
    private ArtifactModuleMappingRepository artifactModuleMappingRepository;

    private LicenseVariationRepository licenseVariationRepository;
    private ArtifactLicenseVariationMappingRepository variationMappingRepository;

    private Module module;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doAction() throws Exception {
        if (skip) {
            log.warn("skipped");
        } else {
            super.doAction();
            final ProjectMetadata projectMetadata = new ProjectMetadata();
            projectMetadata.setProjectId(projectId);
            projectMetadata.setProjectName(projectName);
            projectMetadata.setProjectComponent(projectComponent);

            log.info("##############################################");
            log.info("# ID: " + projectMetadata.getProjectId());
            log.info("# PROJECT: " + projectMetadata.getProjectName());
            log.info("# COMPONENT: " + projectMetadata.getProjectComponent());
            log.info("# VERSION: " + getProject().getVersion());
            log.info("# MODULE: " + getProject().getGroupId() + ":" + getProject().getArtifactId() + ":" + getProject().getPackaging());
            log.info("##############################################");

            log.debug("connecting to database (dryRun: " + dryRun + ")");
            try (final EntityManagerDelegate em = new EntityManagerDelegate(dryRun, createHibernateProperties())) {
                if (!dryRun) {
                    em.begin();
                    initRepos(em);

                    PersistenceHelper.persistProjectStructure(
                            projectRepository,
                            versionRepository,
                            moduleRepository,
                            projectMetadata,
                            getProject()
                    );
                }

                final LicenseMap licenseMap = getLicenseMap();
                log.info("# license mappings");
                for (final String licenseString : licenseMap.keySet()) {
                    log.info("mappings of " + licenseString);
                    for (final MavenProject mavenProject : licenseMap.get(licenseString)) {
                        processDependecy(licenseString, mavenProject, MavenUtil.getMetadataForMavenProject(mavenProject, dependencies));
                    }
                    if (!dryRun) {
                        em.flush();
                        em.clear();
                    }
                }

                if (!dryRun) {
                    em.commit();
                }
            }
        }
    }

    protected void initRepos(final EntityManagerDelegate em) {
        log.debug("initialize repos");
        projectRepository = new ProjectRepository(log, em);
        versionRepository = new VersionRepository(log, em);
        moduleRepository = new ModuleRepository(log, em);

        artifactRepository = new ArtifactRepository(log, em);
        artifactModuleMappingRepository = new ArtifactModuleMappingRepository(log, em);

        licenseVariationRepository = new LicenseVariationRepository(log, em);
        variationMappingRepository = new ArtifactLicenseVariationMappingRepository(log, em);
    }

    protected void processDependecy(final String licenseString, final MavenProject mavenProject, final ArtifactMetadata metadata) {
        String coordinates = MavenUtil.getMavenProjectCoordinates(mavenProject);
        log.info(String.format(" => %s:%s:%s", coordinates, metadata.getScope(), metadata.getType()));
        if (!dryRun) {
            final pm.mbo.license.model.artifact.Artifact artifact = persistDependency(mavenProject, coordinates, metadata);
            final LicenseVariation licenseVariation = persistLicenseVariation(licenseString);

            persistArtifactLicenseVariationMapping(artifact, licenseVariation);
            persistArtifactModuleMapping(artifact);
        }
    }

    protected void persistArtifactModuleMapping(final pm.mbo.license.model.artifact.Artifact artifact) {
        final ArtifactModuleMapping artifactModuleMapping = new ArtifactModuleMapping();
        artifactModuleMapping.setModule(module);
        artifactModuleMapping.setArtifact(artifact);
        artifactModuleMappingRepository.findOrCreate(new FindArtifactModuleMappingByForeignKeysQuery(artifactModuleMapping));
    }

    protected void persistArtifactLicenseVariationMapping(final pm.mbo.license.model.artifact.Artifact artifact,
                                                          final LicenseVariation licenseVariation) {
        final ArtifactLicenseVariationMapping artifactLicenseVariationMapping = new ArtifactLicenseVariationMapping();
        artifactLicenseVariationMapping.setArtifact(artifact);
        artifactLicenseVariationMapping.setLicenseVariation(licenseVariation);
        variationMappingRepository.findOrCreate(
                new FindArtifactLicenseVariationMappingByForeignKeysQuery(artifactLicenseVariationMapping));
    }

    protected LicenseVariation persistLicenseVariation(final String licenseString) {
        final LicenseVariation variation = new LicenseVariation();
        variation.setName(licenseString);

        return licenseVariationRepository.findOrCreate(new FindLicenseVariationByNameQuery(variation));
    }

    protected pm.mbo.license.model.artifact.Artifact persistDependency(final MavenProject mavenProject, final String coordinates, final ArtifactMetadata metadata) {
        pm.mbo.license.model.artifact.Artifact artifact = new pm.mbo.license.model.artifact.Artifact();

        artifact.setMavenGroupId(mavenProject.getGroupId());
        artifact.setMavenArtifactId(mavenProject.getArtifactId());
        artifact.setMavenVersion(mavenProject.getVersion());

        artifact.setMavenCoordinates(coordinates);

        artifact.setMavenScope(metadata.getScope());
        artifact.setMavenType(metadata.getType());

        return artifactRepository.findOrCreate(new FindArtifactByMavenCoordinatesQuery(artifact));
    }

    protected Map<String, String> createHibernateProperties() {
        final HibernateConfigBuilder hibernateConfigBuilder = new HibernateConfigBuilder()
                .url(databaseUrl)
                .user(databaseUser)
                .password(databasePassword)
                .driverClass(databaseDriverClass)
                .dialect(databaseDialect)
                .debug(debug)
                .hbm2ddl(databaseHbm2ddl);
        log.debug(hibernateConfigBuilder.toString());
        return hibernateConfigBuilder.build();
    }
}
