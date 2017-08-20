package pm.mbo.license.model;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import pm.mbo.license.test.DatabaseTest;
import pm.mbo.license.test.EntityTestUtil;

import javax.persistence.metamodel.EntityType;

public class ModelTest extends DatabaseTest {

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
    public void testModel() {
        em.createNativeQuery("SELECT 1").getSingleResult();
    }

    @Test
    public void testEntities() {
        for (EntityType<?> entityType : emf.getMetamodel().getEntities()) {
            EntityTestUtil.checkEntity(entityType.getJavaType());
        }

    }

}
