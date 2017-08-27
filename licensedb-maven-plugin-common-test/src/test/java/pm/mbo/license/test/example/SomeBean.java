package pm.mbo.license.test.example;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table
public class SomeBean extends AbstractBean {

    private String name;

    private SomeEnum blaaa;

}