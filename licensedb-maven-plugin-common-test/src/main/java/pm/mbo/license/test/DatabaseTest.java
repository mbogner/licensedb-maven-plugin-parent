package pm.mbo.license.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.fail;

public abstract class DatabaseTest {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseTest.class);

    protected static EntityManagerFactory emf;
    protected static EntityManager em;

    @Rule
    public final TestName testName = new TestName();

    /**
     * Use this method in @BeforeClass.
     *
     * @throws IOException  No network port available.
     * @throws SQLException Problems with database.
     */
    protected static int initDatabaseTest() throws IOException, SQLException {
        if (null != emf || null != em) {
            fail("already initialized");
        }

        final int databasePort = DatabaseTool.startDatabase();
        NetworkTool.checkPort(databasePort);

        LOG.trace("initialize hibernate");
        emf = Persistence.createEntityManagerFactory("test", getDefaultProperties(databasePort));
        LOG.trace("initialized emf");
        em = emf.createEntityManager();
        LOG.trace("initialize em");

        return databasePort;
    }

    /**
     * Use this method in @AfterClass.
     *
     * @throws SQLException Problems with database.
     */
    public static void shutdownDatabaseTest(final int databasePort) throws SQLException {
        NetworkTool.checkPort(databasePort);

        if (null != em) {
            em.close();
            LOG.trace("closed em");
        }
        if (null != emf) {
            emf.close();
            LOG.trace("closed emf");
        }
        DatabaseTool.stopDatabase(databasePort);

        em = null;
        emf = null;
    }

    @Before
    public void setUp() {
        LOG.trace("starting test {}.{}", this.getClass().getSimpleName(), testName.getMethodName());
        if (null != em) {
            if (em.getTransaction().isActive()) {
                fail("transaction already active");
            }
            em.getTransaction().begin();
        }
    }

    @After
    public void tearDown() {
        if (null != em) {
            if (!em.getTransaction().isActive() || em.getTransaction().getRollbackOnly()) {
                fail("no active or rollbackOnly transaction");
            }
            em.getTransaction().rollback();
        }
        LOG.trace("test {}.{} done", this.getClass().getSimpleName(), testName.getMethodName());
    }


    protected static Properties getDefaultProperties(final int databasePort) {
        NetworkTool.checkPort(databasePort);

        final String connectionString = DatabaseTool.createConnectionString(databasePort);
        LOG.trace("connecting to database: {}", connectionString);
        final Properties props = new Properties();
        props.setProperty("hibernate.connection.url", connectionString);
        props.setProperty("hibernate.connection.username", "sa");
        props.setProperty("hibernate.connection.password", "");

        props.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        props.setProperty("hibernate.connection.driver_class", "org.h2.Driver");

        props.setProperty("hibernate.show_sql", "true");
        props.setProperty("hibernate.format_sql", "true");
        props.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        return props;
    }

}
