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
import pm.mbo.license.model.project.Project;
import pm.mbo.license.model.project.Version;
import pm.mbo.license.model.variation.ArtifactLicenseVariationMapping;
import pm.mbo.license.model.variation.LicenseVariation;
import pm.mbo.license.mojo.dal.EntityManagerDelegate;
import pm.mbo.license.mojo.dal.Repository;
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

    private Repository<Long, Project> projectRepository;
    private Repository<Long, Version> versionRepository;
    private Repository<Long, Module> moduleRepository;

    private Repository<Long, pm.mbo.license.model.artifact.Artifact> artifactRepository;
    private Repository<Long, ArtifactModuleMapping> artifactModuleMappingRepository;

    private Repository<Long, LicenseVariation> licenseVariationRepository;
    private Repository<Long, ArtifactLicenseVariationMapping> variationMappingRepository;

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

                    module = PersistenceHelper.persistProjectStructure(
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
        projectRepository = new Repository<>(log, em);
        versionRepository = new Repository<>(log, em);
        moduleRepository = new Repository<>(log, em);

        artifactRepository = new Repository<>(log, em);
        artifactModuleMappingRepository = new Repository<>(log, em);

        licenseVariationRepository = new Repository<>(log, em);
        variationMappingRepository = new Repository<>(log, em);
    }

    protected void processDependecy(final String licenseString, final MavenProject mavenProject, final ArtifactMetadata metadata) {
        String coordinates = MavenUtil.getMavenProjectCoordinates(mavenProject);
        log.info(String.format(" => %s:%s:%s", coordinates, metadata.getScope(), metadata.getType()));
        if (!dryRun) {
            final pm.mbo.license.model.artifact.Artifact artifact = PersistenceHelper.persistDependency(artifactRepository, mavenProject, coordinates, metadata);
            final LicenseVariation licenseVariation = PersistenceHelper.persistLicenseVariation(licenseVariationRepository, licenseString);

            PersistenceHelper.persistArtifactLicenseVariationMapping(variationMappingRepository, artifact, licenseVariation);
            PersistenceHelper.persistArtifactModuleMapping(artifactModuleMappingRepository, artifact, module);
        }
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
