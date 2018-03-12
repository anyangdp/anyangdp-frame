package com.anyangdp.service;


import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

public interface PageableService<ID, DTO> extends CRUDService<ID, DTO> {

    List<DTO> list(DTO condition);

    List<DTO> listActive();

    List<DTO> list(Example example);

    PageInfo<DTO> listAll(RowBounds rowBounds);

    PageInfo<DTO> list(Example example, RowBounds rowBounds);


}
