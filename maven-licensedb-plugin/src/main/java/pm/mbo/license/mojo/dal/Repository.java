package pm.mbo.license.mojo.dal;

import pm.mbo.license.model.meta.AbstractEntity;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.Serializable;

public class Repository<ID extends Serializable, T extends AbstractEntity<ID>> {

    protected final EntityManagerDelegate emd;

    public Repository(final EntityManagerDelegate emd) {
        if (null == emd) {
            throw new IllegalStateException("emd must not be null");
        }
        this.emd = emd;
    }

    public T merge(T t) {
        return emd.merge(t);
    }

    public void begin() {
        emd.begin();
    }

    public void commit() {
        emd.commit();
    }

    public <R> R findSingle(final QueryDefinition<R> definition) {
        if(null == definition) {
            throw new IllegalArgumentException("definition must not be null");
        }
        final TypedQuery<R> query = emd.createNamedQuery(definition.getQueryName(), definition.getResultClass());
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
            emd.persist(result);
        }
        return result;
    }
}
