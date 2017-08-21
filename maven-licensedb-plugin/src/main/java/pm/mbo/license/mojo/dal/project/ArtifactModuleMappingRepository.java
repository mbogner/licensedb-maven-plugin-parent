package pm.mbo.license.mojo.dal.project;

import org.apache.maven.plugin.logging.Log;
import pm.mbo.license.model.project.ArtifactModuleMapping;
import pm.mbo.license.mojo.dal.AbstractRepository;
import pm.mbo.license.mojo.dal.EntityManagerDelegate;

public class ArtifactModuleMappingRepository extends AbstractRepository<Long, ArtifactModuleMapping> {

    public ArtifactModuleMappingRepository(final Log log, final EntityManagerDelegate em) {
        super(log, em);
    }

    @Override
    protected Class<ArtifactModuleMapping> getManagedClass() {
        return ArtifactModuleMapping.class;
    }
}
