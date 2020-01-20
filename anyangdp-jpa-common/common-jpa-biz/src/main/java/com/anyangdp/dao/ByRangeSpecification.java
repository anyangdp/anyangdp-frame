package com.anyangdp.dao;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Kerry
 * @version: 1.0
 * @date: 17/7/13
 */
public class  ByRangeSpecification<T> implements Specification<T> {
    private final List<RangeQuery> ranges;

    public  ByRangeSpecification(List<RangeQuery> ranges) {
        this.ranges = ranges;
    }

    @Override
    public  Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if(CollectionUtils.isEmpty(ranges)){
            return builder.conjunction();
        }

        List<Predicate> predicates = new ArrayList();

        for (RangeQuery range : ranges) {
            if (range.getLowerBound() != null || range.getUpperBound() != null) {
                Predicate rangePredicate = buildRangePredicate(range, root, builder);

                if (rangePredicate != null) {
                    if (range.getIncludeNull() == null || range.getIncludeNull() == Boolean.FALSE) {
                        predicates.add(rangePredicate);
                    } else {
                        predicates.add(builder.or(rangePredicate, builder.isNull(root.get(range.getField()))));
                    }
                }

                if (Boolean.TRUE == range.getIncludeNull()) {
                    predicates.add(builder.isNull(root.get(range.getField())));
                } else if(Boolean.TRUE == range.getIncludeNull()) {
                    predicates.add(builder.isNotNull(root.get(range.getField())));
                }
            }
        }
        return predicates.isEmpty() ? builder.conjunction() :
                builder.and(predicates.toArray(new Predicate[predicates.size()]));
    }

    private Predicate buildRangePredicate(RangeQuery range, Root<T> root, CriteriaBuilder builder) {

        Predicate greaterThanOrEqualTo = builder.greaterThanOrEqualTo(root.get(range.getField()), range.getLowerBound());
        Predicate lessThanOrEqualTo = builder.lessThanOrEqualTo(root.get(range.getField()), range.getUpperBound());

        if (range.getLowerBound() != null && range.getUpperBound() != null) {

            return builder.and(greaterThanOrEqualTo,lessThanOrEqualTo);
//            return builder.between(root.get(range.getField()), range.getFrom(), range.getTo());
        } else if (range.getLowerBound() != null) {
            return greaterThanOrEqualTo;
        } else if (range.getUpperBound() != null) {
            return lessThanOrEqualTo;
        }
        return null;
    }

}
