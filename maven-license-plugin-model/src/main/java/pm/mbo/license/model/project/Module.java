package pm.mbo.license.model.project;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pm.mbo.license.model.meta.NamedAbstractEntity;

import javax.persistence.*;
import java.util.List;

@Data
@ToString(callSuper = true, exclude = {"artifactModuleMappings"})
@EqualsAndHashCode(callSuper = true, exclude = {"artifactModuleMappings"})
@Entity
@Table(name = "modules", uniqueConstraints = {
        @UniqueConstraint(name = "uc_modules__name_version", columnNames = {"name", "version_id"})
})
public class Module extends NamedAbstractEntity<Long> {

    @ManyToOne(optional = false)
    @JoinColumn(name = "version_id", nullable = false, foreignKey = @ForeignKey(name = "fk_modules__version_id"))
    private Version version;

    @OneToMany(mappedBy = "module")
    private List<ArtifactModuleMapping> artifactModuleMappings;
}
