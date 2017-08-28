package pm.mbo.license.mojo.dal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pm.mbo.license.model.meta.AbstractEntity;
import pm.mbo.license.test.DatabaseTest;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;

public abstract class QueryDefinitionTest<I extends Serializable, E extends AbstractEntity<I>, R,  T extends QueryDefinition<R>>
        extends DatabaseTest {

    protected EntityManagerDelegate emd;
    protected Repository<I, E> repository;
    protected T queryDefinition;

    @Test
    public void getQueryName() throws Exception {
        assertNotNull(getQueryDefinition().getQueryName());
    }

    @Test
    public void getResultClass() throws Exception {
        assertNotNull(getQueryDefinition().getResultClass());
    }

    @Test
    public void testExecuteWithoutData() {
        if (getQueryDefinition().isUpdate()) {
            final Query namedQuery = getEmd().createNamedQuery(getQueryDefinition().getQueryName());
            getQueryDefinition().setParameters(namedQuery);
            namedQuery.executeUpdate();
        } else {
            final TypedQuery<R> namedQuery = getEmd().createNamedQuery(getQueryDefinition().getQueryName(), getQueryDefinition().getResultClass());
            getQueryDefinition().setParameters(namedQuery);
            assertNotNull(namedQuery.getResultList());
        }
    }

    @Test
    public void testNewEntry() throws NoSuchMethodException {
        assertNotNull(getQueryDefinition().newEntry());
    }

    @Before
    @Override
    public void setUp() {
        if (getQueryDefinition().isUpdate()) {
            super.setUp();
        }
        assertNotNull(getQueryDefinition());
    }

    @After
    @Override
    public void tearDown() {
        if (getQueryDefinition().isUpdate()) {
            super.tearDown();
        }
    }

    protected abstract T loadQueryDefinition();

    protected synchronized T getQueryDefinition() {
        if (null == queryDefinition) {
            queryDefinition = loadQueryDefinition();
        }
        return queryDefinition;
    }

    protected synchronized Repository<I, E> getRepositoy() {
        if (null == repository) {
            repository = new Repository<>(getEmd());
        }
        return repository;
    }

    protected synchronized EntityManagerDelegate getEmd() {
        if (null == emd) {
            emd = new EntityManagerDelegate(emf, em);
        }
        return emd;
    }

    public static int initDatabaseTest() throws IOException, SQLException {
        return DatabaseTest.initDatabaseTest("mojo");
    }

}