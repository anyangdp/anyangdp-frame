package com.anyangdp.dao;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface BaseRepository<T,ID extends Serializable> extends JpaRepository<T,ID>{
    List<T> findAll(Example<T> example, List<RangeQuery> ranges);

    Page<T> findAll(Example<T> example, List<RangeQuery> ranges, Pageable pageable);

    List<T> findAll(Example<T> example, SearchQuery searchQuery);

    Page<T> findAll(Example<T> example, SearchQuery searchQuery, Pageable pageable);

    List<T> findAll(Example<T> example, List<RangeQuery> ranges, SearchQuery searchQuery);

    Page<T> findAll(Example<T> example, List<RangeQuery> ranges, SearchQuery searchQuery, Pageable pageable);

    List<T> findAll(T entity, LikeQuery likeQuery);

    Page<T> findAll(T entity, LikeQuery likeQuery, Pageable pageable);

    List<T> findAll(T entity, List<RangeQuery> ranges, LikeQuery likeQuery);

    Page<T> findAll(T entity, List<RangeQuery> ranges, LikeQuery likeQuery, Pageable pageable);

    Page<T> findAllBySql(String sql, Pageable pageable, Object...params);

    long count(Example<T> example, List<RangeQuery> ranges);
}
