package pm.mbo.license.mojo.dal;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertNull;

public class EntityManagerDelegateDryRunTest {

    private EntityManagerDelegate emd;

    @Before
    public void setUp() {
        emd = new EntityManagerDelegate(true, Collections.emptyMap());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEntityManagerDelegate() {
        new EntityManagerDelegate(true, null);
    }

    @Test
    public void persist() throws Exception {
        emd.persist(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void persist_null() throws Exception {
        emd.persist(null);
    }

    @Test
    public void merge() throws Exception {
        assertNull(emd.merge(new Object()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void merge_null() throws Exception {
        emd.merge(null);
    }

    @Test
    public void find() throws Exception {
        assertNull(emd.find(Object.class, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void find_null() throws Exception {
        emd.find(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void find_null1() throws Exception {
        emd.find(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void find_null2() throws Exception {
        emd.find(Object.class, null);
    }

    @Test
    public void getReference() throws Exception {
        assertNull(emd.getReference(Object.class, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getReference_null() throws Exception {
        emd.getReference(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getReference_null1() throws Exception {
        emd.getReference(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getReference_null2() throws Exception {
        emd.getReference(Object.class, null);
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

    @Test(expected = IllegalArgumentException.class)
    public void createNamedQuery_null() throws Exception {
        emd.createNamedQuery(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNamedQuery_null1() throws Exception {
        emd.createNamedQuery(null, String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNamedQuery_null2() throws Exception {
        emd.createNamedQuery("asd", null);
    }

    @Test
    public void createUntypedNamedQuery() throws Exception {
        assertNull(emd.createNamedQuery("asd"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createUntypedNamedQuery_null() throws Exception {
        emd.createNamedQuery(null);
    }

    @Test
    public void close() throws Exception {
        emd.close();
    }

}