package com.anyangdp.frame.service;

import com.anyangdp.frame.handler.AbstractDO;
import com.anyangdp.frame.handler.AbstractDTO;
import com.anyangdp.frame.util.CommonUtil;
import com.anyangdp.frame.util.ReflectionUtils;
import com.anyangdp.frame.util.SnowFlake;
import com.anyangdp.frame.util.ValueUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.Function;

/**
 * @Author anyang
 * @CreateTime 2019/11/25
 * @Des
 */
public abstract class AbstractService<DTO extends AbstractDTO, M extends BaseMapper<DO>,  DO extends AbstractDO> extends ServiceImpl<M, DO> implements CRUDService<DTO, DO> {

    private Class<DO> doClass;

    private Class<DTO> dtoClass;

    @Resource
    private SnowFlake snowFlake;

    public AbstractService() {
        super();
        dtoClass = ReflectionUtils.getClassGenricType(getClass(), 0);
        doClass = ReflectionUtils.getClassGenricType(getClass(), 2);
    }

    @Override
    public DTO insert(DTO entity) {
        DO target = ValueUtil.dump(entity, doClass);
        target.setId(snowFlake.nextId());
        save(target);
        entity = ValueUtil.dump(target, dtoClass);
        return entity;
    }

    @Override
    public DTO retrieve(Long id) {
        DO byId = super.getById(id);
        return ValueUtil.dump(byId, dtoClass);
    }

    @Override
    public DTO retrieveByCondition(DTO condition) {
        QueryWrapper<DO> queryWrapper = new QueryWrapper<>();
        queryWrapper.allEq(CommonUtil.objectToMapLowerUnderScoreKey(condition), false);
        DO one = getOne(queryWrapper);
        return ValueUtil.dump(one, dtoClass);
    }

    @Override
    public boolean update(DTO entity) {
        DO dump = ValueUtil.dump(entity, doClass);
        return updateById(dump);
    }

    @Override
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public boolean logicDelete(Long id) {
        try {
            DO aDo = doClass.newInstance();
            aDo.setDeleted(true);
            aDo.setId((Long) id);
            boolean b = updateById(aDo);
            return b;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean active(Long id) {
        try {
            DO aDo = doClass.newInstance();
            aDo.setActive(true);
            aDo.setId(id);
            boolean b = updateById(aDo);
            return b;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deActive(Long id) {
        try {
            DO aDo = doClass.newInstance();
            aDo.setActive(false);
            aDo.setId(id);
            boolean b = updateById(aDo);
            return b;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<DTO> list(DTO condition) {
        DO dump = ValueUtil.dump(condition, doClass);
        QueryWrapper<DO> queryWrapper = new QueryWrapper<>();
        queryWrapper.allEq(CommonUtil.objectToMapLowerUnderScoreKey(dump), false);
        return ValueUtil.dumpList(list(queryWrapper), dtoClass);
    }

    @Override
    public IPage<DTO> page(IPage<DO> page, DTO condition) {
        DO dump = ValueUtil.dump(condition, doClass);
        QueryWrapper<DO> queryWrapper = new QueryWrapper<>();
        queryWrapper.allEq(CommonUtil.objectToMapLowerUnderScoreKey(dump), false);
        Function<DO, DTO> fcPage = (n) -> ValueUtil.dump(n, dtoClass);
        IPage<DTO> dtoIPage = page(page, queryWrapper).convert(fcPage);
        return dtoIPage;
    }
}
