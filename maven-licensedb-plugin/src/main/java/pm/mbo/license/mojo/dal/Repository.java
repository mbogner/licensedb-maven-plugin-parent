package pm.mbo.license.mojo.dal;

import org.apache.maven.plugin.logging.Log;
import pm.mbo.license.model.meta.AbstractEntity;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.Serializable;

public class Repository<ID extends Serializable, T extends AbstractEntity<ID>> {

    protected final Log log;
    protected final EntityManagerDelegate em;

    public Repository(final Log log, final EntityManagerDelegate em) {
        this.log = log;
        this.em = em;
    }

    public T merge(T t) {
        return em.merge(t);
    }

    public void begin() {
        em.begin();
    }

    public void commit() {
        em.commit();
    }

    public <R> R findSingle(final QueryDefinition<R> definition) {
        final TypedQuery<R> query = em.createNamedQuery(definition.getQueryName(), definition.getResultClass());
        definition.setParameters(query);
        try {
            return query.getSingleResult();
        } catch (final NoResultException exc) {
            return null;
        }
    }

    public <R> R findOrCreate(final QueryDefinition<R> definition) {
        R result = findSingle(definition);
        if (null == result) {
            result = definition.newEntry();
            em.persist(result);
        }
        return result;
    }
}