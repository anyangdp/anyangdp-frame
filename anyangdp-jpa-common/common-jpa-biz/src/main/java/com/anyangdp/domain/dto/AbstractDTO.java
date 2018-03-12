package com.anyangdp.domain.dto;

import com.anyangdp.service.IdentifierAwareDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Setter
@Getter
public abstract class AbstractDTO<ID extends Serializable> implements IdentifierAwareDTO<ID> {

    private ID id;

    private Timestamp creationDate;
    private Integer createdBy;
    private Integer lastUpdatedBy;
    private Integer sort;
    private String deleted;
    private String enabled;
    private Timestamp lastUpdatedDate;

}
