package pm.mbo.license.mojo.dal.variation.query;

import pm.mbo.license.model.variation.LicenseVariation;
import pm.mbo.license.mojo.dal.QueryDefinition;

import javax.persistence.Query;

public class FindLicenseVariationByNameQuery extends QueryDefinition<LicenseVariation> {

    private final LicenseVariation licenseVariation;

    public FindLicenseVariationByNameQuery(final LicenseVariation licenseVariation) {
        this.licenseVariation = licenseVariation;
    }

    @Override
    public void setParameters(final Query query) {
        query.setParameter("name", licenseVariation.getName());
    }

    @Override
    public LicenseVariation newEntry() {
        return licenseVariation;
    }
}
