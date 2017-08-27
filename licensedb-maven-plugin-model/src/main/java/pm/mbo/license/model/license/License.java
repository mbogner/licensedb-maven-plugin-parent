package pm.mbo.license.model.license;

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
@ToString(callSuper = true, exclude = {"patterns", "artifactLicenseMappings"})
@EqualsAndHashCode(callSuper = true, exclude = {"patterns", "artifactLicenseMappings"})
@Entity
@Table(name = "licenses", uniqueConstraints = {
        @UniqueConstraint(name = "uc_licenses__name", columnNames = {"name"})
})
public class License extends NamedAbstractEntity<Long> {

    @OneToMany(mappedBy = "license")
    private List<LicensePattern> patterns;

    @OneToMany(mappedBy = "license")
    private List<ArtifactLicenseMapping> artifactLicenseMappings;

}
