package com.anyangdp.dao;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BaseRepositoryImpl<T,ID extends Serializable>
        extends SimpleJpaRepository<T,ID> implements BaseRepository<T,ID> {
    private final JpaEntityInformation<T, ?> entityInformation;
    private final EntityManager em;

    public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.em = entityManager;
    }

    @Override
    public Page<T> findAllBySql(String sql, Pageable pageable, Object... params) {
        Class<T> domainClass = entityInformation.getJavaType();
        Query query = em.createNativeQuery(sql, domainClass);
        if (params != null) {
            for (int i = 1; i <= params.length; i++) {
                query.setParameter(i, params[i-1]);
            }
        }

        if (pageable == null) {
            return new PageImpl<T>(query.getResultList());
        }

        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        String countSql = "select count(*) from (" + sql + ") t";
        Query countQuery = em.createNativeQuery(countSql);
        if (params != null) {
            for (int i = 1; i <= params.length; i++) {
                countQuery.setParameter(i, params[i-1]);
            }
        }

        BigInteger total = (BigInteger) countQuery.getSingleResult();
        List<T> content = total.intValue() > pageable.getOffset() ? query.getResultList() : Collections.emptyList();

        return new PageImpl(content, pageable, total.longValue());
    }

    @Override
    public List<T> findAll(Example<T> example, List<RangeQuery> ranges) {
        return internalSearch(example, ranges, null, null).getContent();
    }

    @Override
    public Page<T> findAll(Example<T> example, List<RangeQuery> ranges, Pageable pageable) {
        return internalSearch(example, ranges, null, pageable);
    }

    @Override
    public List<T> findAll(Example<T> example, SearchQuery searchQuery) {
        return internalSearch(example, null, searchQuery, null).getContent();
    }

    @Override
    public Page<T> findAll(Example<T> example, SearchQuery searchQuery, Pageable pageable) {
        return internalSearch(example, null, searchQuery, pageable);
    }

    @Override
    public List<T> findAll(Example<T> example, List<RangeQuery> ranges, SearchQuery searchQuery) {
        return internalSearch(example, ranges, searchQuery, null).getContent();
    }

    @Override
    public Page<T> findAll(Example<T> example, List<RangeQuery> ranges, SearchQuery searchQuery, Pageable pageable) {
        return internalSearch(example, ranges, searchQuery, pageable);
    }

    @Override
    public List<T> findAll(T entity, LikeQuery likeQuery) {
        return internalLike(entity, null, likeQuery, null).getContent();
    }

    @Override
    public Page<T> findAll(T entity, LikeQuery likeQuery, Pageable pageable) {
        return internalLike(entity, null, likeQuery, pageable);
    }

    @Override
    public List<T> findAll(T entity, List<RangeQuery> ranges, LikeQuery likeQuery) {
        return internalLike(entity, ranges, likeQuery, null).getContent();
    }

    @Override
    public Page<T> findAll(T entity, List<RangeQuery> ranges, LikeQuery likeQuery, Pageable pageable) {
        return internalLike(entity, ranges, likeQuery, pageable);
    }

    @Override
    public long count(Example<T> example, List<RangeQuery> ranges) {

        Specification<T> byExample = new ByExampleSpecification<>(example);
        Specification<T> byRanges = new ByRangeSpecification<>(ranges);
        return count(Specifications.where(byExample).and(byRanges));
    }

    private Page<T> internalSearch(Example<T> example, List<RangeQuery> ranges, SearchQuery searchQuery, Pageable pageable) {

        Specifications<T> specification = null;
        if (null != example) {
            Specification<T> byExample = new ByExampleSpecification<>(example);
            specification = Specifications.where(byExample);
        }
        if (!CollectionUtils.isEmpty(ranges)) {
            Specification<T> byRanges = new ByRangeSpecification<>(ranges);
            specification = specification.and(byRanges);
        }
        if (null != searchQuery) {
            Specification<T> byFullTextSearch = new ByFullTextSearchSpecification<>(searchQuery);
            specification = specification.and(byFullTextSearch);
        }
        return findAll(specification, pageable);
    }

    private Page<T> internalLike(T entity, List<RangeQuery> ranges, LikeQuery likeQuery, Pageable pageable) {

        ExampleMatcher matcher = ExampleMatcher.matching();
        if (Objects.nonNull(likeQuery)) {

            for (String field : likeQuery.getFields()) {
                matcher = matcher.withMatcher(field, m -> {
                    if (Boolean.TRUE == likeQuery.getStartsWith()) {
                        m.startsWith();
                    } else if (Boolean.FALSE == likeQuery.getStartsWith()) {
                        m.endsWith();
                    } else {
                        m.contains();
                    }
                });
            }
        }

        Example<T> example = Example.of(entity, matcher);

        Specification<T> byExample = new ByExampleSpecification<>(example);
        Specification<T> byRanges = new ByRangeSpecification<>(ranges);
        return findAll(Specifications.where(byExample).and(byRanges), pageable);
    }

}
