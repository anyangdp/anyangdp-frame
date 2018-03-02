package com.anyangdp.service;

public interface CRUDService<ID,DTO> {
    /**
     * 根据id查找实体类
     *
     * @param id 逐渐
     * @return 实体类
     */
    DTO searchByPk(ID id);

    /**
     * 根据查询条件查找实体类
     *
     * @param condition 实体类查询对象
     * @return 实体类列表
     */
    DTO searchByCondition(DTO condition);

    /**
     * 添加数据
     * @param dto
     * @return
     */
    DTO insert(DTO dto);

    boolean update(DTO dto);

    boolean delete(ID id);

    boolean active(DTO dto);

    boolean deActive(DTO dto);

    int count(DTO condition);

    /**
     * 判断是否存在
     * @param condition
     * @return
     */
    boolean exist(DTO condition);
}
