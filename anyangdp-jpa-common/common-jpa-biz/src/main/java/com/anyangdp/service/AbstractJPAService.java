package com.anyangdp.service;


import com.anyangdp.dao.BaseDao;
import com.anyangdp.domain.AbstractPersistableEntity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public abstract class AbstractJPAService<ID extends Serializable, DTO extends IdentifierAwareDTO, ENTITY extends AbstractPersistableEntity<ID>, DAO extends BaseDao<ENTITY, ID>>
        extends AbstractStatelessJPAService<ID, DTO, ENTITY, DAO> {

    @Override
    protected void deleteHandler(ENTITY target) {
        target.setDeleted("1");
        target.setLastUpdatedDate(new Timestamp(new Date().getTime()));
    }
}
