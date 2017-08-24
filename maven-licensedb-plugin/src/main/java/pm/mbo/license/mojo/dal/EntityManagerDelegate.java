package pm.mbo.license.mojo.dal;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import pm.mbo.license.mojo.helper.Conditions;

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
        Conditions.notNull("properties", properties);
        if (useEm) {
            emf = Persistence.createEntityManagerFactory("mojo", properties);
            em = emf.createEntityManager();
        }
    }

    public void persist(final Object obj) {
        Conditions.notNull("obj", obj);
        if (useEm) {
            em.persist(obj);
        }
    }

    public <T> T merge(final T obj) {
        Conditions.notNull("obj", obj);
        if (useEm) {
            return em.merge(obj);
        }
        return null;
    }

    public <T> T find(final Class<T> aClass, final Object obj) {
        Conditions.notNull("aClass", aClass);
        Conditions.notNull("obj", obj);
        if (useEm) {
            return em.find(aClass, obj);
        }
        return null;
    }

    public <T> T getReference(final Class<T> aClass, final Object obj) {
        Conditions.notNull("aClass", aClass);
        Conditions.notNull("obj", obj);
        if (useEm) {
            return em.getReference(aClass, obj);
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

    public <T> TypedQuery<T> createNamedQuery(final String queryName, final Class<T> resultType) {
        Conditions.notNull("queryName", queryName);
        Conditions.notNull("resultType", resultType);
        if (useEm) {
            return em.createNamedQuery(queryName, resultType);
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
