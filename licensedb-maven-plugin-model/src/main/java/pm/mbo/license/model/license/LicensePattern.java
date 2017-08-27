package pm.mbo.license.model.license;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pm.mbo.license.model.meta.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "license_patterns")
public class LicensePattern extends AbstractEntity<Long> {

    @ManyToOne(optional = false)
    @JoinColumn(name = "license_id", nullable = false)
    private License license;

    @NotBlank
    private String groupdIdRegex;

    @NotBlank
    private String artifactIdRegex;

    @NotBlank
    private String versionRegex;

}
