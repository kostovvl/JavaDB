package entities;

import javax.persistence.*;

@MappedSuperclass
public class BaseEntity {
    private long id;

    protected BaseEntity() {
    }

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
