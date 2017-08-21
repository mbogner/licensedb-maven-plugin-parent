package pm.mbo.license.mojo.dal.artifact;

import org.apache.maven.plugin.logging.Log;
import pm.mbo.license.model.artifact.Artifact;
import pm.mbo.license.mojo.dal.AbstractRepository;
import pm.mbo.license.mojo.dal.EntityManagerDelegate;

public class ArtifactRepository extends AbstractRepository<Long, Artifact> {

    public ArtifactRepository(final Log log, final EntityManagerDelegate em) {
        super(log, em);
    }

    @Override
    protected Class<Artifact> getManagedClass() {
        return Artifact.class;
    }
}
