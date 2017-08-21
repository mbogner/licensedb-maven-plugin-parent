package pm.mbo.license.mojo.dal.variation;

import org.apache.maven.plugin.logging.Log;
import pm.mbo.license.model.variation.ArtifactLicenseVariationMapping;
import pm.mbo.license.mojo.dal.AbstractRepository;
import pm.mbo.license.mojo.dal.EntityManagerDelegate;

public class ArtifactLicenseVariationMappingRepository extends AbstractRepository<Long, ArtifactLicenseVariationMapping> {

    public ArtifactLicenseVariationMappingRepository(final Log log, final EntityManagerDelegate em) {
        super(log, em);
    }

    @Override
    protected Class<ArtifactLicenseVariationMapping> getManagedClass() {
        return ArtifactLicenseVariationMapping.class;
    }
}
