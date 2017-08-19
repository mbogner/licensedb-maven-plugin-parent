package pm.mbo.license.model.project;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pm.mbo.license.model.meta.NamedAbstractEntity;

import javax.persistence.*;
import java.util.List;

@Data
@ToString(callSuper = true, exclude = {"modules"})
@EqualsAndHashCode(callSuper = true, exclude = {"modules"})
@Entity
@Table(name = "versions", uniqueConstraints = {
        @UniqueConstraint(name = "uc_versions__name_project", columnNames = {"name", "project_id"})
})
public class Version extends NamedAbstractEntity<Long> {

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToMany(mappedBy = "version")
    private List<Module> modules;

}
