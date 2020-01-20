package com.anyangdp.domain.dto;

import com.anyangdp.dao.LikeQuery;
import com.anyangdp.dao.RangeQuery;
import com.anyangdp.dao.SearchQuery;
import com.anyangdp.service.IdentifierAwareDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Setter
@Getter
public abstract class AbstractDTO<ID extends Serializable> implements IdentifierAwareDTO<ID> {

    private ID id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime creationDate;

    private Integer createdBy;
    private Integer lastUpdatedBy;
    private Integer sort;
    private String deleted;
    private String enabled;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime lastUpdatedDate;

    private SearchQuery searchQuery;
    private LikeQuery likeQuery;
    private List<RangeQuery> rangeQuerys;

    public void addRangeQuery(RangeQuery rangeQuery){
        if (rangeQuerys == null)
            rangeQuerys = new ArrayList<>();
        rangeQuerys.add(rangeQuery);
    }

}
