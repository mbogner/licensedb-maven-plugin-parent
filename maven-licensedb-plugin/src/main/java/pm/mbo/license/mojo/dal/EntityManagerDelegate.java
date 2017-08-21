package pm.mbo.license.mojo.dal;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.io.Closeable;
import java.util.Map;

public class EntityManagerDelegate implements Closeable {

    private EntityManagerFactory emf;
    private EntityManager em;

    public EntityManagerDelegate(final boolean dryRun, final Map<String, String> properties) {
        if (!dryRun) {
            emf = Persistence.createEntityManagerFactory("mojo", properties);
            em = emf.createEntityManager();
        }
    }

    public void persist(Object o) {
        if (em != null) {
            em.persist(o);
        }
    }

    public <T> T merge(T t) {
        if (em != null) {
            return em.merge(t);
        }
        return null;
    }

    public <T> T find(Class<T> aClass, Object o) {
        if (em != null) {
            return em.find(aClass, o);
        }
        return null;
    }

    public <T> T getReference(Class<T> aClass, Object o) {
        if (em != null) {
            return em.getReference(aClass, o);
        }
        return null;
    }

    public void flush() {
        if (em != null) {
            em.flush();
        }
    }

    public void clear() {
        if (em != null) {
            em.clear();
        }
    }

    public void begin() {
        if (em != null) {
            em.getTransaction().begin();
        }
    }

    public void commit() {
        if (em != null) {
            em.getTransaction().commit();
        }
    }

    public <T> TypedQuery<T> createNamedQuery(String s, Class<T> aClass) {
        if (em != null) {
            return em.createNamedQuery(s, aClass);
        }
        return null;
    }

    @Override
    public void close() {
        if (null != em) {
            em.close();
        }
        if (null != emf) {
            emf.close();
        }
    }
}
