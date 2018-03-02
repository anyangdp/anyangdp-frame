package com.anyangdp.service;

import com.anyangdp.utils.ReflectionUtils;
import com.anyangdp.utils.ValueUtil;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractStatelessService<ID extends Serializable,
        DTO extends IdentifierAwareDTO,
        ENTITY,
        DAO extends tk.mybatis.mapper.common.Mapper<ENTITY>>
        extends AbstractService<ID, DTO> {

    protected Class<ENTITY> entityClass;

    private Class<DAO> daoClass;

    @Autowired
    protected tk.mybatis.mapper.common.Mapper<ENTITY> dao;

    private final static ExampleMatcher DEFAULT_STRING_MATCHER = ExampleMatcher.matching();

    public AbstractStatelessService() {
        super();
        entityClass = ReflectionUtils.getClassGenricType(getClass(),2);
        daoClass = ReflectionUtils.getClassGenricType(getClass(), 3);
    }

    /**
     * pk查询
     * @param id
     * @return
     */
    @Override
    public DTO searchByPk(ID id) {
        ENTITY entity = dao.selectByPrimaryKey(id);
        if (null == entity) {
            log.warn("search entity not found --- id : {} ", id);
        }
        return ValueUtil.dump(entity, dtoClass);
    }

    /**
     * 条件查询
     * @param condition 实体类查询对象
     * @return
     */
    @Override
    public DTO searchByCondition(DTO condition) {
        ENTITY entity = dao.selectOne(ValueUtil.dump(condition, entityClass));
        return ValueUtil.dump(entity, dtoClass);
    }

    /**
     * 条件查询(实体简单查询查询)
     * @param condition
     * @return
     */
    @Override
    public List<DTO> list(DTO condition) {
        ENTITY prod = ValueUtil.dump(condition, entityClass);
        List<DTO> data = dao.select(prod).stream().map(entity -> ValueUtil.dump(entity, dtoClass)).collect(Collectors.toList());
        return data;
    }

    /**
     * 查询正常用户
     * @return
     */
    @Override
    public List<DTO> listActive() {
        ENTITY prod = null;
        List<DTO> data = dao.select(prod).stream().map(entity -> ValueUtil.dump(entity, dtoClass)).collect(Collectors.toList());
        return data;
    }

    /**
     * 条件查询，复杂查询
     * @param example
     * @return
     */
    @Override
    public List<DTO> list(Example example) {
        List<DTO> data = dao.selectByExample(example).stream().map(entity -> ValueUtil.dump(entity, dtoClass)).collect(Collectors.toList());
        return data;
    }

    @Override
    public boolean update(DTO dto) {
        ENTITY entity = ValueUtil.dump(dto, entityClass);
        return dao.updateByPrimaryKeySelective(entity) > 0;
    }

    /**
     * 逻辑恢复
     * @param dto
     * @return
     */
    @Override
    public boolean active(DTO dto) {
        return dao.updateByPrimaryKeySelective(ValueUtil.dump(dto, entityClass)) > 0;
    }

    /**
     * 逻辑删除
     * @param dto
     * @return
     */
    @Override
    public boolean deActive(DTO dto) {
        return dao.updateByPrimaryKeySelective(ValueUtil.dump(dto, entityClass)) > 0;
    }

    @Override
    public int count(DTO condition) {
        ENTITY entity = ValueUtil.dump(condition, entityClass);
        return dao.selectCount(entity);
    }

    /**
     * 单表添加
     * @param dto
     * @return
     */
    @Override
    public DTO insert(DTO dto) {
        dao.insert(ValueUtil.dump(dto, entityClass));
        return ValueUtil.dump(dto, dtoClass);
    }

    /**
     * 物理删除
     * @param id
     * @return
     */
    @Override
    public boolean delete(ID id) {
        if (null == this.searchByPk(id)) {
            return false;
        }
        return dao.deleteByPrimaryKey(id) > 0 ;
    }

    @Override
    public boolean exist(DTO condition) {
        ENTITY prod = ValueUtil.dump(condition, entityClass);
        int count = dao.selectCount(prod);
        if (count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 分页查询全表
     * @param rowBounds
     * @return
     */
    @Override
    public PageInfo<DTO> listAll(RowBounds rowBounds) {
        List<ENTITY> list = dao.selectByRowBounds(null, new RowBounds(rowBounds.getOffset() - 1,rowBounds.getLimit()));
        List<DTO> dtoList = list.stream().map(entity -> ValueUtil.dump(entity, dtoClass)).collect(Collectors.toList());
        PageInfo<DTO> page = new PageInfo<>(dtoList);
        return page;
    }


    @Override
    public PageInfo<DTO> list(Example example, RowBounds rowBounds) {
        List<ENTITY> list = dao.selectByExampleAndRowBounds(example, new RowBounds(rowBounds.getOffset() - 1,rowBounds.getLimit()));
        List<DTO> dtoList = list.stream().map(entity -> ValueUtil.dump(entity, dtoClass)).collect(Collectors.toList());
        PageInfo<DTO> page = new PageInfo<>(dtoList);
        return page;
    }
}
