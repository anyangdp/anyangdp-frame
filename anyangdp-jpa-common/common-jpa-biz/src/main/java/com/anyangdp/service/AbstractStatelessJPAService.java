package com.anyangdp.service;

import com.anyangdp.dao.BaseDao;
import com.anyangdp.domain.AbstractPersistableEntity;
import com.anyangdp.utils.ReflectionUtils;
import com.anyangdp.utils.ValueUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractStatelessJPAService<ID extends Serializable,
        DTO extends IdentifierAwareDTO,
        ENTITY extends AbstractPersistableEntity<ID>,
        DAO extends BaseDao<ENTITY, ID>>
        extends AbstractService<ID, DTO> implements PageableService<ID, DTO> {

    protected Class<ENTITY> entityClass;

    private Class<DAO> daoClass;

    @Autowired
    protected BaseDao<ENTITY, ID> dao;

    AbstractStatelessJPAService() {
        super();
        entityClass = ReflectionUtils.getClassGenricType(getClass(), 2);
        daoClass = ReflectionUtils.getClassGenricType(getClass(), 3);
    }

    abstract protected void deleteHandler(ENTITY target);

    /**
     * id查询
     *
     * @param id
     * @return
     */
    @Override
    public DTO retrieve(ID id) {
        ENTITY aa = dao.findOne(id);
        return ValueUtils.dump(aa, dtoClass);
    }

    /**
     * 条件查询单个实体
     * @param condition
     * @return
     */
    @Override
    public DTO retrieveByCondition(DTO condition) {
        ENTITY prob = ValueUtils.dump(condition, entityClass);
        Example<ENTITY> example = Example.of(prob);
        return ValueUtils.dump(dao.findOne(example),dtoClass);
    }

    /**
     * 添加
     *
     * @param dto
     * @return
     */
    @Override
    public DTO insert(DTO dto) {
        ENTITY entity = ValueUtils.dump(dto, entityClass);
        dao.save(entity);
        return ValueUtils.dump(entity, dtoClass);
    }

    /**
     * 修改
     *
     * @param dto
     * @return
     */
    @Override
    public boolean update(DTO dto) {
        ENTITY entity = ValueUtils.dump(dto, entityClass);
        if (exists(dto)) {
            dao.save(entity);
        } else {
            return false;
        }
        return true;
    }

    /**
     * 激活
     * @param id
     * @return
     */
    @Override
    public boolean active(ID id) {
        return this.toggleActive(id, "1");
    }

    /**
     * 禁用
     * @param id
     * @return
     */
    @Override
    public boolean deactive(ID id) {
        return this.toggleActive(id, "0");
    }

    private boolean toggleActive(ID id, String active) {

        if (!dao.exists(id)) {
            log.warn("entity not found --- id : {} ", id);
            return false;
        }
        ENTITY entity = dao.findOne(id);
        entity.setEnabled(active);
        return true;
    }

    /**
     * 逻辑删除
     * @param id
     * @return
     */
    @Override
    public boolean delete(ID id) {
        if (!dao.exists(id)) {
            log.warn("delete entity not found --- id : {} ", id);
            return false;
        }
        return this.logicDelete(id);
    }

    private boolean logicDelete(ID id) {
        ENTITY entity = dao.findOne(id);
        deleteHandler(entity);
        return true;
    }

    /**
     * 判断是否存在
     * @param condition
     * @return
     */
    @Override
    public boolean exists(DTO condition) {
        ENTITY entity = ValueUtils.dump(condition, entityClass);
        Example<ENTITY> example = Example.of(entity);
        return dao.exists(example);
    }

    @Override
    public Page<DTO> listActive(Pageable pageable) {
        Page<DTO> data = null;
        try {
            ENTITY probe = entityClass.newInstance();
            probe.setEnabled("1");
            probe.setDeleted("0");
            Example example = Example.of(probe);
            Page<ENTITY> vv = dao.findAll(example, pageable);
            data = dao.findAll(example, pageable).map(entity -> ValueUtils.dump(entity, dtoClass));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public List<DTO> listAllActive() {
        return dao.findAll().stream().map(entity -> ValueUtils.dump(entity, dtoClass)).collect(Collectors.toList());
    }

    /**
     * 分页全部数据查询
     * @param pageable
     * @return
     */
    @Override
    public Page<DTO> listAll(Pageable pageable) {
        return dao.findAll(pageable).map(entity -> ValueUtils.dump(entity, dtoClass));
    }

    /**
     * 分页条件查询
     * @param condition
     * @param pageable
     * @return
     */
    @Override
    public Page<DTO> list(DTO condition, Pageable pageable) {
        ENTITY probe = ValueUtils.dump(condition, entityClass);
        Example example = Example.of(probe);
        return dao.findAll(example,pageable).map(entity -> ValueUtils.dump(entity, dtoClass));
    }

    /**
     * 列表查
     * @param condition
     * @return
     */
    @Override
    public List<DTO> list(DTO condition) {
        ENTITY probe = ValueUtils.dump(condition, entityClass);
        Example<ENTITY> example = Example.of(probe);
        return dao.findAll(example).stream().map(entity -> ValueUtils.dump(entity, dtoClass)).collect(Collectors.toList());
    }
}
