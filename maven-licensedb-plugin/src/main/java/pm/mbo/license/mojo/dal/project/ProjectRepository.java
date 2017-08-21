package pm.mbo.license.mojo.dal.project;

import org.apache.maven.plugin.logging.Log;
import pm.mbo.license.model.project.Project;
import pm.mbo.license.mojo.dal.AbstractRepository;
import pm.mbo.license.mojo.dal.EntityManagerDelegate;

public class ProjectRepository extends AbstractRepository<Long, Project> {

    public ProjectRepository(final Log log, final EntityManagerDelegate em) {
        super(log, em);
    }

    @Override
    protected Class<Project> getManagedClass() {
        return Project.class;
    }
}
