package com.anyangdp.service;

public interface IdentifierAwareDTO<ID> {

    ID getId();

    void setId(ID id);
}
