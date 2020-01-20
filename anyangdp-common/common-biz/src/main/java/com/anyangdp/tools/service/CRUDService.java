package com.anyangdp.tools.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author anyang
 * @CreateTime 2019/11/25
 * @Des
 */
public interface CRUDService<DTO, T> extends IService<T> {
    /**
     * 插入一条记录
     *
     * @param entity 实体对象
     */
    DTO insert(DTO entity);

    DTO retrieve(Long id);

    DTO retrieveByCondition(DTO condition);

    boolean update(DTO entity);

    boolean delete(Long id);

    boolean logicDelete(Long id);

    boolean active(Long id);

    boolean deActive(Long id);

    List<DTO> list(DTO condition);

    IPage<DTO> page(IPage<T> page, DTO condition);
}
