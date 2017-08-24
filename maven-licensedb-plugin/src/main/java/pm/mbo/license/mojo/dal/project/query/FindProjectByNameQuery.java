package pm.mbo.license.mojo.dal.project.query;

import pm.mbo.license.model.project.Project;
import pm.mbo.license.mojo.dal.QueryDefinition;

import javax.persistence.Query;

public class FindProjectByNameQuery extends QueryDefinition<Project> {

    private final Project project;

    public FindProjectByNameQuery(final Project project) {
        this.project = project;
    }

    @Override
    public void setParameters(final Query query) {
        query.setParameter("name", project.getName());
    }

    @Override
    public Project newEntry() {
        return project;
    }

    @Override
    public Class<Project> getResultClass() {
        return Project.class;
    }
}
