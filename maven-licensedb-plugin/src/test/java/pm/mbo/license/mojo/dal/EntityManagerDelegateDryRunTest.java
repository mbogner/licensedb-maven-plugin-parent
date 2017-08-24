package pm.mbo.license.mojo.dal;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertNull;

public class EntityManagerDelegateDryRunTest {

    private EntityManagerDelegate emd;

    @Before
    public void setUp() {
        emd = new EntityManagerDelegate(true, Collections.EMPTY_MAP);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEntityManagerDelegate() {
        new EntityManagerDelegate(true, null);
    }

    @Test
    public void persist() throws Exception {
        emd.persist(new Object());
    }

    @Test
    public void merge() throws Exception {
        assertNull(emd.merge(new Object()));
    }

    @Test
    public void find() throws Exception {
        assertNull(emd.find(Object.class, 1));
    }

    @Test
    public void getReference() throws Exception {
        assertNull(emd.getReference(Object.class, 1));
    }

    @Test
    public void flush() throws Exception {
        emd.flush();
    }

    @Test
    public void clear() throws Exception {
        emd.clear();
    }

    @Test
    public void begin() throws Exception {
        emd.begin();
    }

    @Test
    public void commit() throws Exception {
        emd.commit();
    }

    @Test
    public void createNamedQuery() throws Exception {
        assertNull(emd.createNamedQuery("asd", String.class));
    }

    @Test
    public void close() throws Exception {
        emd.close();
    }

}