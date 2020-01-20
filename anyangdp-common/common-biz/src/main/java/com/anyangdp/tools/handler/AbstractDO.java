package com.anyangdp.tools.handler;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author anyang
 * @CreateTime 2019/10/9
 * @Des 底层通用抽象实体
 */
@Setter
@Getter
@Accessors(chain = true)
public abstract class AbstractDO implements Serializable {
    private static final long serialVersionUID = -5756045370916112553L;
    @TableId(type = IdType.INPUT)
    private Long id;

    private Long createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private Long updatedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    private Long deletedBy;

    private String deletedMsg;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deletedAt;

    private boolean active = true;

    private boolean deleted = false;
}
