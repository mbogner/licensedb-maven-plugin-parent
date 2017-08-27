package pm.mbo.license.model.project;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pm.mbo.license.model.meta.DateHelper;
import pm.mbo.license.model.meta.NamedAbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Calendar;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, exclude = {"versions"})
@Entity
@Table(name = "projects", uniqueConstraints = {
        @UniqueConstraint(name = "uc_projects__key", columnNames = {"key"}),
        @UniqueConstraint(name = "uc_projects__name_component", columnNames = {"name", "component"})
})
public class Project extends NamedAbstractEntity<Long> {

    @NotBlank
    @Column(name = "key", nullable = false)
    private String key;

    @Column(name = "component")
    private String component;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_build")
    private Calendar lastBuild;

    @OneToMany(mappedBy = "project")
    private List<Version> versions;

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("key", key)
                .append("component", component)
                .append("lastBuild", DateHelper.printable(lastBuild))
                .toString();
    }
}
