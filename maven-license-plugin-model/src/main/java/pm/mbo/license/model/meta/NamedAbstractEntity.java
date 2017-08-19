package pm.mbo.license.model.meta;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class NamedAbstractEntity<T extends Serializable> extends AbstractEntity<T> {

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

}
