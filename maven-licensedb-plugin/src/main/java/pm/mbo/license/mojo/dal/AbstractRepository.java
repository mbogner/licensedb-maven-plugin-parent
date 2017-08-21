package pm.mbo.license.mojo.dal;

import org.apache.maven.plugin.logging.Log;
import pm.mbo.license.model.meta.AbstractEntity;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.Serializable;

public abstract class AbstractRepository<ID extends Serializable, T extends AbstractEntity<ID>> {

    protected final Log log;
    protected final EntityManagerDelegate em;

    public AbstractRepository(final Log log, final EntityManagerDelegate em) {
        this.log = log;
        this.em = em;
    }

    protected abstract Class<T> getManagedClass();

    public T merge(T t) {
        return em.merge(t);
    }

    public void begin() {
        em.begin();
    }

    public void commit() {
        em.commit();
    }

    public T findSingle(final QueryDefinition<T> definition) {
        final TypedQuery<T> query = em.createNamedQuery(definition.getQueryName(), getManagedClass());
        definition.setParameters(query);
        try {
            return query.getSingleResult();
        } catch (final NoResultException exc) {
            return null;
        }
    }

    public T findOrCreate(final QueryDefinition<T> definition) {
        T result = findSingle(definition);
        if (null == result) {
            result = definition.newEntry();
            em.persist(result);
        }
        return result;
    }
}
