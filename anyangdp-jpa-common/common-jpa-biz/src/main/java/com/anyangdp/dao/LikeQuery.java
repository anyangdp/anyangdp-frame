package com.anyangdp.dao;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LikeQuery implements Serializable{
    private static final long serialVersionUID = 1L;

    private String[] fields;
    private Boolean startsWith;

    public LikeQuery(Boolean startsWith, String...fields) {
        this.startsWith = startsWith;
        this.fields = fields;
    }

    public LikeQuery(String...fields) {
        this.fields = fields;
    }
}