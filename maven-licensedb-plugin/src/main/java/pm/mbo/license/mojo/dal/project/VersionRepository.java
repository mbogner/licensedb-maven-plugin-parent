package pm.mbo.license.mojo.dal.project;

import org.apache.maven.plugin.logging.Log;
import pm.mbo.license.model.project.Version;
import pm.mbo.license.mojo.dal.AbstractRepository;
import pm.mbo.license.mojo.dal.EntityManagerDelegate;

public class VersionRepository extends AbstractRepository<Long, Version> {

    public VersionRepository(final Log log, final EntityManagerDelegate em) {
        super(log, em);
    }

    @Override
    protected Class<Version> getManagedClass() {
        return Version.class;
    }
}
