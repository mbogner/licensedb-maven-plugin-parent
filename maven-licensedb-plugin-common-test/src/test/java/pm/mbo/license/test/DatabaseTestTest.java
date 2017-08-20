package pm.mbo.license.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseTestTest extends DatabaseTest {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseTestTest.class);

    private static int databasePort;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        databasePort = DatabaseTest.initDatabaseTest();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        DatabaseTest.shutdownDatabaseTest(databasePort);
    }

    @Test
    public void testSomething1() {
        LOG.info("someTest1");
    }

    @Test
    public void testSomething2() {
        LOG.info("someTest2");
    }

}