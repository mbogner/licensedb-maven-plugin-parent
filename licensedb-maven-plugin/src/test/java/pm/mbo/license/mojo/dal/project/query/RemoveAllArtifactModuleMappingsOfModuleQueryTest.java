package pm.mbo.license.mojo.dal.project.query;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import pm.mbo.license.model.project.ArtifactModuleMapping;
import pm.mbo.license.model.project.Module;
import pm.mbo.license.mojo.dal.QueryDefinitionTest;
import pm.mbo.license.test.DatabaseTest;

public class RemoveAllArtifactModuleMappingsOfModuleQueryTest
        extends QueryDefinitionTest<Long, ArtifactModuleMapping, Integer, RemoveAllArtifactModuleMappingsOfModuleQuery> {

    private static int databasePort;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        databasePort = QueryDefinitionTest.initDatabaseTest();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        DatabaseTest.shutdownDatabaseTest(databasePort);
    }

    @Test(expected = IllegalStateException.class)
    @Override
    public void testNewEntry() throws NoSuchMethodException {
        getQueryDefinition().newEntry();
    }

    @Override
    protected RemoveAllArtifactModuleMappingsOfModuleQuery loadQueryDefinition() {
        final Module module = new Module();
        module.setId(-1L);

        return new RemoveAllArtifactModuleMappingsOfModuleQuery(module);
    }

}