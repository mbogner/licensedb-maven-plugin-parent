package pm.mbo.license.model.variation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pm.mbo.license.model.artifact.Artifact;
import pm.mbo.license.model.meta.AbstractEntity;

import javax.persistence.*;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "artifact_license_variation_mappings", uniqueConstraints = {
        @UniqueConstraint(name = "uc_artifact_license_variation_mappings__key", columnNames = {"artifact_id", "license_variation_id"}),
})
public class ArtifactLicenseVariationMapping extends AbstractEntity<Long> {

    @ManyToOne(optional = false)
    @JoinColumn(name = "artifact_id", nullable = false, foreignKey = @ForeignKey(name = "fk_artifact_license_variation_mappings__artifact_id"))
    private Artifact artifact;

    @ManyToOne(optional = false)
    @JoinColumn(name = "license_variation_id", nullable = false, foreignKey = @ForeignKey(name = "fk_artifact_license_variation_mappings__license_variation_id"))
    private LicenseVariation licenseVariation;
}
