package com.anyangdp.damain.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author william
 */
@Getter
@Setter
@MappedSuperclass
public abstract class AbstractPersistableEntity<ID extends Serializable> implements Persistable<ID> {

    private static final long serialVersionUID = -1L;

    public abstract void setId(ID id);

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否删除 0否 1是
     */
    @Column(insertable = false)
    private String deleted;

    /**
     * 是否启用 0否 1是
     */
    @Column(insertable = false)
    private String enabled;

    /**
     * 创建时间
     */
    @Column(insertable = false)
    private Timestamp creationDate;

    /**
     * 创建人
     */
    private Integer createdBy;

    /**
     * 最后编辑时间
     */
    @Column(insertable = false)
    private Timestamp LastUpdatedDate;

    /**
     * 最后编辑人
     */
    private Integer lastUpdatedBy;

    @Transient
    public boolean isNew() {
        return null == this.getId();
    }
}


