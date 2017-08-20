package pm.mbo.license.model.meta;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;

@Data
@MappedSuperclass
public abstract class AbstractEntity<T extends Serializable> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private T id;

    @Version
    @Column(name = "lock_version")
    private Long lockVersion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Calendar createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", insertable = false)
    private Calendar updatedAt;

    @PrePersist
    private void prePersist() {
        createdAt = Calendar.getInstance();
    }

    @PreUpdate
    private void preUpdate() {
        updatedAt = Calendar.getInstance();
    }

}
