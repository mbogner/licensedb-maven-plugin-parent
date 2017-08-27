package pm.mbo.license.model.project;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pm.mbo.license.model.meta.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@ToString(callSuper = true, exclude = {"artifactModuleMappings"})
@EqualsAndHashCode(callSuper = true, exclude = {"artifactModuleMappings"})
@Entity
@Table(name = "modules", uniqueConstraints = {
        @UniqueConstraint(name = "uc_modules__maven_coordinates", columnNames = {"full_coordinates"}),
        @UniqueConstraint(name = "uc_modules__maven_group_id_maven_version_version_id", columnNames = {"maven_group_id", "maven_artifact_id", "version_id"}),
})
public class Module extends AbstractEntity<Long> {

    /**
     * Used to simplify queries: project.name:groupId:artifactId:version.name
     */
    @NotBlank
    @Column(name = "full_coordinates", nullable = false)
    private String fullCoordinates;

    @NotBlank
    @Column(name = "maven_group_id", nullable = false)
    private String mavenGroupId;

    @NotBlank
    @Column(name = "maven_artifact_id", nullable = false)
    private String mavenArtifactId;

    @NotBlank
    @Column(name = "maven_packaging", nullable = false)
    private String mavenPackaging;

    @ManyToOne(optional = false)
    @JoinColumn(name = "version_id", nullable = false, foreignKey = @ForeignKey(name = "fk_modules__version_id"))
    private Version version;

    @OneToMany(mappedBy = "module")
    private List<ArtifactModuleMapping> artifactModuleMappings;

    public String createFullCoordinates() {
        if(null == version || null == version.getProject()) {
            throw new IllegalStateException("version and version.project have to be set");
        }
        return String.format("%s:%s:%s:%s",
                version.getProject().getName(),
                mavenGroupId,
                mavenArtifactId,
                version.getName());
    }

}
