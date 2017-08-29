package pm.mbo.license.mojo.dal.artifact.query;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import pm.mbo.license.model.artifact.Artifact;
import pm.mbo.license.mojo.dal.QueryDefinitionTest;
import pm.mbo.license.test.DatabaseTest;

public class FindArtifactByMavenCoordinatesQueryTest
        extends QueryDefinitionTest<Long, Artifact, Artifact, FindArtifactByMavenCoordinatesQuery> {

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
    protected FindArtifactByMavenCoordinatesQuery loadQueryDefinition() {
        final Artifact artifact = new Artifact();
        artifact.setMavenCoordinates("bla:bla:bla");
        return new FindArtifactByMavenCoordinatesQuery(artifact);
    }
}