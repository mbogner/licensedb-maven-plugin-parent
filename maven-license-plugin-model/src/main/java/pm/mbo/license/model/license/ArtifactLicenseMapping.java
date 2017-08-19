package pm.mbo.license.model.license;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pm.mbo.license.model.meta.AbstractEntity;

import javax.persistence.*;
import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "artifact_license_mappings", uniqueConstraints = {
        @UniqueConstraint(name = "uc_artifact_license_mappings__artifact_license", columnNames = {"artifact_id", "license_id"}),
})
public class ArtifactLicenseMapping extends AbstractEntity<Long> {

    @ManyToOne(optional = false)
    @JoinColumn(name = "artifact_id", nullable = false)
    private Artifact artifact;

    @ManyToOne(optional = false)
    @JoinColumn(name = "license_id", nullable = false)
    private License license;

}
