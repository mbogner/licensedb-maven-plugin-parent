package pm.mbo.license.mojo.dal.project.query;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import pm.mbo.license.model.project.Version;
import pm.mbo.license.mojo.dal.QueryDefinitionTest;
import pm.mbo.license.test.DatabaseTest;

public class FindVersionByProjectAndNameQueryTest
        extends QueryDefinitionTest<Long, Version, Version, FindVersionByProjectAndNameQuery> {

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
    protected FindVersionByProjectAndNameQuery loadQueryDefinition() {
        final Version version = new Version();
// TODO
        final FindVersionByProjectAndNameQuery def = new FindVersionByProjectAndNameQuery(version);
        return def;
    }
}