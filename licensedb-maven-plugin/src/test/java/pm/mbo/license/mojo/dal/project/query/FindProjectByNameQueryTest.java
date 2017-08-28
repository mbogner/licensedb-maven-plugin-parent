package pm.mbo.license.mojo.dal.project.query;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import pm.mbo.license.model.project.Project;
import pm.mbo.license.mojo.dal.QueryDefinitionTest;
import pm.mbo.license.test.DatabaseTest;

public class FindProjectByNameQueryTest
        extends QueryDefinitionTest<Long, Project, Project, FindProjectByNameQuery> {

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
    protected FindProjectByNameQuery loadQueryDefinition() {
        // TODO
        return null;
    }
}