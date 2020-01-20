package com.anyangdp.service;

import com.anyangdp.dao.LikeQuery;
import com.anyangdp.dao.RangeQuery;
import com.anyangdp.dao.SearchQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PageableService<ID,DTO> extends CRUDService<ID,DTO> {
    Page<DTO> listActive(Pageable pageable);

    List<DTO> listAllActive();

    Page<DTO> listAll(Pageable pageable);

    Page<DTO> list(DTO condition, Pageable pageable);

    List<DTO> list(DTO condition);

    Page<DTO> list(DTO condition, List<RangeQuery> ranges, SearchQuery searchQuery, Pageable pageable);

    Page<DTO> list(DTO condition, List<RangeQuery> ranges, LikeQuery likeQuery, Pageable pageable);
}
