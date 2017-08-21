package pm.mbo.license.mojo.dal;

import javax.persistence.Query;

public abstract class QueryDefinition<T> {

    public String getQueryName() {
        return this.getClass().getName();
    }

    public abstract void setParameters(final Query query);

    public abstract T newEntry();
}
