package com.anyangdp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseDao<T,ID extends Serializable> extends JpaRepository<T,ID>, CrudRepository<T,ID> {
}
