package pm.mbo.license.model.variation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pm.mbo.license.model.meta.NamedAbstractEntity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.List;

@Data
@ToString(callSuper = true, exclude = {"artifactLicenseVariationMappings"})
@EqualsAndHashCode(callSuper = true, exclude = {"artifactLicenseVariationMappings"})
@Entity
@Table(name = "license_variations", uniqueConstraints = {
        @UniqueConstraint(name = "uc_license_variations__name", columnNames = {"name"}),
})
public class LicenseVariation extends NamedAbstractEntity<Long> {

    @OneToMany(mappedBy = "licenseVariation")
    private List<ArtifactLicenseVariationMapping> artifactLicenseVariationMappings;

}
