package com.anyangdp.dao;

/**
 * @author: Kerry
 * @version: 1.0
 * @date: 17/7/13
 */

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.Assert;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * {@link Specification} that gives access to the {@link Predicate} instance representing the values contained in the
 * {@link Example}.
 *
 * @author Christoph Strobl
 * @since 1.10
 * @param <T>
 */
public class ByExampleSpecification<T> implements Specification<T> {

    private final Example<T> example;

    /**
     * Creates new {@link SimpleJpaRepository.ExampleSpecification}.
     *
     * @param example
     */
    public ByExampleSpecification(Example<T> example) {

        Assert.notNull(example, "Example must not be null!");
        this.example = example;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.domain.Specification#toPredicate(javax.persistence.criteria.Root, javax.persistence.criteria.CriteriaQuery, javax.persistence.criteria.CriteriaBuilder)
     */
    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return QueryByExamplePredicateBuilder.getPredicate(root, cb, example);
    }
}