package com.anyangdp.domain.dto;

import com.anyangdp.service.IdentifierAwareDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Setter
@Getter
public abstract class AbstractDTO<ID extends Serializable> implements IdentifierAwareDTO<ID> {

    private ID id;

    private Timestamp creationDate;
    private Integer createdBy;
    private Integer lastUpdatedBy;
    private Integer sort;
    private String deleted = "0";
    private String enabled = "1";
    private Timestamp lastUpdatedDate;

}
