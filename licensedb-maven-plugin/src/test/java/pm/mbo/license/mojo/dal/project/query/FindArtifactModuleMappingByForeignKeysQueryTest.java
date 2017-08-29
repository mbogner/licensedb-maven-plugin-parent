package pm.mbo.license.mojo.dal.project.query;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import pm.mbo.license.model.artifact.Artifact;
import pm.mbo.license.model.project.ArtifactModuleMapping;
import pm.mbo.license.model.project.Module;
import pm.mbo.license.mojo.dal.QueryDefinitionTest;
import pm.mbo.license.test.DatabaseTest;

public class FindArtifactModuleMappingByForeignKeysQueryTest
        extends QueryDefinitionTest<Long, ArtifactModuleMapping, ArtifactModuleMapping, FindArtifactModuleMappingByForeignKeysQuery> {

    private static int databasePort;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        databasePort = QueryDefinitionTest.initDatabaseTest();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        DatabaseTest.shutdownDatabaseTest(databasePort);
    }

    @Override
    protected FindArtifactModuleMappingByForeignKeysQuery loadQueryDefinition() {
        final ArtifactModuleMapping mapping = new ArtifactModuleMapping();
        mapping.setArtifact(new Artifact());
        mapping.getArtifact().setId(-1L);
        mapping.setModule(new Module());
        mapping.getModule().setId(-1L);

        return new FindArtifactModuleMappingByForeignKeysQuery(mapping);
    }
}