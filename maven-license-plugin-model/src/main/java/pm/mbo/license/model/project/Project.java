package pm.mbo.license.model.project;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pm.mbo.license.model.meta.NamedAbstractEntity;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

@Data
@ToString(callSuper = true, exclude = {"versions"})
@EqualsAndHashCode(callSuper = true, exclude = {"versions"})
@Entity
@Table(name = "projects", uniqueConstraints = {
        @UniqueConstraint(name = "uc_projects__name_component", columnNames = {"name", "component"})
})
public class Project extends NamedAbstractEntity<Long> {

    @Column(name = "component")
    private String component;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_build")
    private Calendar lastBuild;

    @OneToMany(mappedBy = "project")
    private List<Version> versions;

}
