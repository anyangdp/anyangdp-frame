package com.anyangdp.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;
import java.sql.Timestamp;

@Setter
@Getter
@MappedSuperclass
public abstract class AbstractPersistableEntity<ID extends Serializable> implements Persistable<ID> {

    @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp creationDate;
    private Integer createdBy;
    private Integer lastUpdatedBy;
    private Integer sort;
    @Column(columnDefinition = "char(1)")
    private String deleted;
    @Column(columnDefinition = "char(1)")
    private String enabled;
    @Column(insertable = false, updatable = false)
    private Timestamp lastUpdatedDate;

    public abstract void setId(ID id);

    @Transient
    @Override
    public boolean isNew() {
        return null == this.getId();
    }
}

