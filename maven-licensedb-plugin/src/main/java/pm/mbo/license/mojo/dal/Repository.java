package pm.mbo.license.mojo.dal;

import pm.mbo.license.model.meta.AbstractEntity;
import pm.mbo.license.mojo.helper.Conditions;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.Serializable;

public class Repository<ID extends Serializable, T extends AbstractEntity<ID>> {

    protected final EntityManagerDelegate emd;

    public Repository(final EntityManagerDelegate emd) {
        Conditions.notNull("emd", emd);
        this.emd = emd;
    }

    public T merge(T t) {
        Conditions.notNull("t", t);
        return emd.merge(t);
    }

    public void begin() {
        emd.begin();
    }

    public void commit() {
        emd.commit();
    }

    public <R> R findSingle(final QueryDefinition<R> definition) {
        Conditions.notNull("definition", definition);
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

    public int executeUpdate(final QueryDefinition<Integer> definition) {
        Conditions.notNull("definition", definition);
        final Query query = emd.createNamedQuery(definition.getQueryName());
        definition.setParameters(query);
        return query.executeUpdate();
    }
}
