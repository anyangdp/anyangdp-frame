package com.anyangdp.dao;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;

public abstract class BaseDaoImpl<T,ID extends Serializable>
        extends SimpleJpaRepository<T,ID> implements BaseDao<T,ID> {
    public BaseDaoImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }
}
