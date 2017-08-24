package pm.mbo.license.mojo.dal.variation.query;

import pm.mbo.license.model.variation.ArtifactLicenseVariationMapping;
import pm.mbo.license.mojo.dal.QueryDefinition;

import javax.persistence.Query;

public class FindArtifactLicenseVariationMappingByForeignKeysQuery extends QueryDefinition<ArtifactLicenseVariationMapping> {

    private final ArtifactLicenseVariationMapping artifactLicenseVariationMapping;

    public FindArtifactLicenseVariationMappingByForeignKeysQuery(final ArtifactLicenseVariationMapping artifactLicenseVariationMapping) {
        this.artifactLicenseVariationMapping = artifactLicenseVariationMapping;
    }

    @Override
    public void setParameters(final Query query) {
        query.setParameter("artifact_id", artifactLicenseVariationMapping.getArtifact().getId());
        query.setParameter("license_variation_id", artifactLicenseVariationMapping.getLicenseVariation().getId());
    }

    @Override
    public ArtifactLicenseVariationMapping newEntry() {
        return artifactLicenseVariationMapping;
    }

    @Override
    public Class<ArtifactLicenseVariationMapping> getResultClass() {
        return ArtifactLicenseVariationMapping.class;
    }
}
