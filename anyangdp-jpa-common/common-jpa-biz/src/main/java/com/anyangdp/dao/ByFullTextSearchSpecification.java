package com.anyangdp.dao;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description alter table table add fulltext index ngram_idx(field) with parser ngram;
 * @author Kerry
 * @date 17/7/19 17:49
 */
public class ByFullTextSearchSpecification<T> implements Specification<T> {

    private final SearchQuery searchQuery;

    public  ByFullTextSearchSpecification(SearchQuery searchQuery) {
        this.searchQuery = searchQuery;
    }

    @Override
    public  Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if(searchQuery == null ||  StringUtils.isEmpty(searchQuery.getKeyword())){
            return builder.conjunction();
        }

        List<Predicate> predicates = new ArrayList();
        /*Path<T>[] paths = new Path[searchQuery.getFields().length];
        for (int i = 0; i < searchQuery.getFields().length; i++) {
            paths[i] = root.get(searchQuery.getFields()[i]);
        }*/
//        String fields = StringUtil.join(searchQuery.getFields(), ",");

        Path<String> field = root.get(searchQuery.getFields()[0]);
        Expression<String> keyword = builder.literal( searchQuery.getKeyword());

        //TODO 暂时支持一个字段
        Expression<Double> match = builder.function("match", Double.class, field,keyword);

        predicates.add(builder.greaterThan(match,0D));

        return predicates.isEmpty() ? builder.conjunction() :
                builder.and(predicates.toArray(new Predicate[predicates.size()]));
    }

}