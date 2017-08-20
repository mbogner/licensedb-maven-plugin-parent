package pm.mbo.license.test.example;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@MappedSuperclass
public abstract class AbstractBean implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @PreUpdate
    private void preMerge() {

    }

    @PrePersist
    private void prePersist() {

    }

}
