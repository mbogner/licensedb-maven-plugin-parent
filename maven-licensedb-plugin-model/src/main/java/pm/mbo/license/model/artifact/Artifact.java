package pm.mbo.license.model.artifact;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pm.mbo.license.model.license.ArtifactLicenseMapping;
import pm.mbo.license.model.meta.AbstractEntity;
import pm.mbo.license.model.project.ArtifactModuleMapping;
import pm.mbo.license.model.variation.ArtifactLicenseVariationMapping;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ToString(callSuper = true, exclude = {"artifactLicenseMappings", "artifactModuleMappings", "artifactLicenseVariationMappings"})
@EqualsAndHashCode(callSuper = true, exclude = {"artifactLicenseMappings", "artifactModuleMappings", "artifactLicenseVariationMappings"})
@Entity
@Table(name = "artifacts", uniqueConstraints = {
        @UniqueConstraint(name = "uc_artifacts__maven_coordinates", columnNames = {"maven_coordinates"}),
        @UniqueConstraint(name = "uc_artifacts__maven_coordinates_single", columnNames = {"maven_group_id", "maven_artifact_id", "maven_version"})
})
public class Artifact extends AbstractEntity<Long> {

    @NotBlank
    @Column(name = "maven_coordinates", nullable = false)
    private String mavenCoordinates;

    @NotBlank
    @Column(name = "maven_group_id", nullable = false)
    private String mavenGroupId;

    @NotBlank
    @Column(name = "maven_artifact_id", nullable = false)
    private String mavenArtifactId;

    @NotBlank
    @Column(name = "maven_version", nullable = false)
    private String mavenVersion;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Scope scope;

    @OneToMany(mappedBy = "artifact")
    private List<ArtifactLicenseVariationMapping> artifactLicenseVariationMappings;

    @OneToMany(mappedBy = "artifact")
    private List<ArtifactLicenseMapping> artifactLicenseMappings;

    @OneToMany(mappedBy = "artifact")
    private List<ArtifactModuleMapping> artifactModuleMappings;

}
