package pm.mbo.license.mojo.dal.project.query;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import pm.mbo.license.model.project.Module;
import pm.mbo.license.mojo.dal.QueryDefinitionTest;
import pm.mbo.license.test.DatabaseTest;

public class FindModuleByVersionAndMavenCoordinatesQueryTest
        extends QueryDefinitionTest<Long, Module, Module, FindModuleByVersionAndMavenCoordinatesQuery> {

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
    protected FindModuleByVersionAndMavenCoordinatesQuery loadQueryDefinition() {
        // TODO
        return null;
    }
}