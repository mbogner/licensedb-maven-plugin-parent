package pm.mbo.license.mojo.dal.project.query;

import pm.mbo.license.model.project.Version;
import pm.mbo.license.mojo.dal.QueryDefinition;

import javax.persistence.Query;

public class FindVersionByProjectAndNameQuery extends QueryDefinition<Version> {

    private final Version version;

    public FindVersionByProjectAndNameQuery(final Version version) {
        this.version = version;
    }

    @Override
    public void setParameters(final Query query) {
        query.setParameter("project_id", version.getProject().getId());
        query.setParameter("name", version.getName());
    }

    @Override
    public Version newEntry() {
        return version;
    }

    @Override
    public Class<Version> getResultClass() {
        return Version.class;
    }
}
