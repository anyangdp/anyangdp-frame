package com.anyangdp.dao;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SearchQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    private String[] fields;
    private String keyword;

    public SearchQuery(String keyword, String...fields) {
        this.fields = fields;
        this.keyword = keyword;
    }
}