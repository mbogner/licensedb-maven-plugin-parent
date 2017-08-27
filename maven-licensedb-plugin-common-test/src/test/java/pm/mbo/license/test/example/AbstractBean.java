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

    @SuppressWarnings("EmptyMethod")
    @PreUpdate
    private void preMerge() {
    }

    @SuppressWarnings("EmptyMethod")
    @PrePersist
    private void prePersist() {
    }

    protected void someMethod() {
        // do nothing
    }

}
