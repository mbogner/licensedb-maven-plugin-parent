package pm.mbo.license.mojo;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.license.AddThirdPartyMojo;
import org.codehaus.mojo.license.model.LicenseMap;
import pm.mbo.license.model.artifact.Artifact;
import pm.mbo.license.model.project.Module;
import pm.mbo.license.model.variation.LicenseVariation;
import pm.mbo.license.mojo.dal.EntityManagerDelegate;
import pm.mbo.license.mojo.dal.HibernateConfigBuilder;
import pm.mbo.license.mojo.dal.PersistenceHelper;
import pm.mbo.license.mojo.helper.MavenHelper;
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

    @Parameter(property = "licensedb.eatExceptions", defaultValue = "true")
    private boolean eatExceptions;

    @Parameter(property = "project.artifacts", required = true, readonly = true)
    private Set<org.apache.maven.artifact.Artifact> dependencies;

    @Component
    private PersistenceHelper persistenceHelper;

    @Component
    private MavenHelper mavenHelper;

    private final Log log = getLog();

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doAction() throws Exception {
        if (skip) {
            log.warn("skipped");
        } else {
            if (eatExceptions) {
                try {
                    export();
                } catch (final Throwable e) { // catch everything bad happening in this mojo
                    log.error(e);
                }
            } else {
                export();
            }
        }
    }

    private void export() throws Exception {
        super.doAction();
        final ProjectMetadata projectMetadata = new ProjectMetadata(projectId, projectName, projectComponent);

        log.info("##############################################");
        log.info("# PROJECT");
        log.info("#  - ID: " + projectMetadata.getProjectId());
        log.info("#  - NAME: " + projectMetadata.getProjectName());
        log.info("#  - COMPONENT: " + projectMetadata.getProjectComponent());
        log.info("# VERSION: " + getProject().getVersion());
        log.info("# MODULE: " + getProject().getGroupId() + ":" + getProject().getArtifactId() + ":" + getProject().getPackaging());
        log.info("##############################################");

        if (dryRun) {
            log.warn("dryRun - skip database");
        } else {
            log.info("connecting to database");
        }

        try (final EntityManagerDelegate emd = new EntityManagerDelegate(dryRun, createHibernateProperties())) {
            Module module = null;

            if (!dryRun) {
                emd.begin();
                persistenceHelper.initRepos(emd);

                module = persistenceHelper.persistProjectStructure(
                        projectMetadata,
                        getProject()
                );
                final int removedMappings = persistenceHelper.removeArtifactModuleMappingsOf(module);
                log.info(String.format("removed %d old mappings", removedMappings));
            }

            final LicenseMap licenseMap = getLicenseMap();
            log.info("# license mappings");
            for (final String licenseString : licenseMap.keySet()) {
                log.info("mappings of " + licenseString);
                for (final MavenProject mavenProject : licenseMap.get(licenseString)) {
                    processDependecy(licenseString, mavenProject, module, mavenHelper.getMetadataForMavenProject(mavenProject, dependencies));
                }
                if (!dryRun) {
                    emd.flush();
                    emd.clear();
                }
            }

            if (!dryRun) {
                emd.commit();
            }
        }
    }

    protected void processDependecy(final String licenseString, final MavenProject mavenProject, final Module module, final ArtifactMetadata metadata) {
        final String coordinates = mavenHelper.getMavenProjectCoordinates(mavenProject);
        log.info(String.format(" => %s:%s:%s", coordinates, metadata.getScope(), metadata.getType()));
        if (!dryRun) {
            final Artifact artifact = persistenceHelper.persistDependency(mavenProject, coordinates, metadata);
            final LicenseVariation licenseVariation = persistenceHelper.persistLicenseVariation(licenseString);

            persistenceHelper.persistArtifactLicenseVariationMapping(artifact, licenseVariation);
            persistenceHelper.persistArtifactModuleMapping(artifact, module);
        }
    }

    protected Map<String, String> createHibernateProperties() {
        if (null == databasePassword) {
            databasePassword = "";
        }
        final HibernateConfigBuilder hibernateConfigBuilder = HibernateConfigBuilder.builder()
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
