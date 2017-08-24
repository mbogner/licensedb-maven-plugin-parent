package pm.mbo.license.mojo.dal;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.io.Closeable;
import java.util.Map;

public class EntityManagerDelegate implements Closeable {

    private static final Log LOG = new SystemStreamLog();

    private final boolean useEm;
    private EntityManagerFactory emf;
    private EntityManager em;

    public EntityManagerDelegate(final boolean dryRun, final Map<String, String> properties) {
        useEm = !dryRun;
        if (null == properties) {
            throw new IllegalArgumentException("properties must not be null");
        }
        if (useEm) {
            emf = Persistence.createEntityManagerFactory("mojo", properties);
            em = emf.createEntityManager();
        }
    }

    public void persist(Object o) {
        if (useEm) {
            em.persist(o);
        }
    }

    public <T> T merge(T t) {
        if (useEm) {
            return em.merge(t);
        }
        return null;
    }

    public <T> T find(Class<T> aClass, Object o) {
        if (useEm) {
            return em.find(aClass, o);
        }
        return null;
    }

    public <T> T getReference(Class<T> aClass, Object o) {
        if (useEm) {
            return em.getReference(aClass, o);
        }
        return null;
    }

    public void flush() {
        if (useEm) {
            em.flush();
        }
    }

    public void clear() {
        if (useEm) {
            em.clear();
        }
    }

    public void begin() {
        if (useEm) {
            em.getTransaction().begin();
        }
    }

    public void commit() {
        if (useEm) {
            em.getTransaction().commit();
        }
    }

    public <T> TypedQuery<T> createNamedQuery(String s, Class<T> aClass) {
        if (useEm) {
            return em.createNamedQuery(s, aClass);
        }
        return null;
    }

    @Override
    public void close() {
        if (useEm) {
            if (null != em) {
                em.close();
            }
            if (null != emf) {
                emf.close();
            }
        }
    }
}
