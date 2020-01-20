package com.anyangdp.dao;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RangeQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    private String field;
    private Comparable lowerBound;
    private Comparable upperBound;
    private Boolean includeNull;

    public RangeQuery() {
    }

    public RangeQuery(String field) {
        this(field, null, null, null);
    }

    public RangeQuery(String field, Comparable lowerBound, Comparable upperBound) {
        this(field, lowerBound, upperBound, null);
    }

    public RangeQuery(String field, Comparable lowerBound, Comparable upperBound, Boolean includeNull) {
        this.field = field;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.includeNull = includeNull;
    }

}