package pm.mbo.license.model.project;

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
@Table(name = "artifact_module_mappings", uniqueConstraints = {
        @UniqueConstraint(name = "uc_artifact_module_mappings__artifact_module", columnNames = {"artifact_id", "module_id"}),
})
public class ArtifactModuleMapping extends AbstractEntity<Long> {

    @ManyToOne(optional = false)
    @JoinColumn(name = "artifact_id", nullable = false, foreignKey = @ForeignKey(name = "fk_artifact_module_mappings__artifact_id"))
    private Artifact artifact;

    @ManyToOne(optional = false)
    @JoinColumn(name = "module_id", nullable = false, foreignKey = @ForeignKey(name = "fk_artifact_module_mappings__module_id"))
    private Module module;

}
