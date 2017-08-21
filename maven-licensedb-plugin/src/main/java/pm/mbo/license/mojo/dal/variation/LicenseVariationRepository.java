package pm.mbo.license.mojo.dal.variation;

import org.apache.maven.plugin.logging.Log;
import pm.mbo.license.model.variation.LicenseVariation;
import pm.mbo.license.mojo.dal.AbstractRepository;
import pm.mbo.license.mojo.dal.EntityManagerDelegate;

public class LicenseVariationRepository extends AbstractRepository<Long, LicenseVariation> {

    public LicenseVariationRepository(final Log log, final EntityManagerDelegate em) {
        super(log, em);
    }

    @Override
    protected Class<LicenseVariation> getManagedClass() {
        return LicenseVariation.class;
    }
}
