package com.anyangdp.service;

public interface CRUDService<ID,DTO> {

    DTO create(DTO dto);

    DTO retrieve(ID id);

    DTO retrieveByCondition(DTO condition);

    boolean update(DTO dto);

    boolean delete(ID id);

    boolean active(ID id);

    boolean deactive(ID id);

    boolean exists(DTO condition);

    long count(DTO condition);

}
