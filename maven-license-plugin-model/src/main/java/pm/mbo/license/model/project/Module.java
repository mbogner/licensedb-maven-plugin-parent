package pm.mbo.license.model.project;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pm.mbo.license.model.meta.NamedAbstractEntity;

import javax.persistence.*;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "modules", uniqueConstraints = {
        @UniqueConstraint(name = "uc_modules__name_version", columnNames = {"name", "version_id"})
})
public class Module extends NamedAbstractEntity<Long> {

    @ManyToOne(optional = false)
    @JoinColumn(name = "version_id", nullable = false)
    private Version version;

}
