package com.anyangdp.service;

import com.anyangdp.utils.ReflectionUtils;
import lombok.Setter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import java.io.Serializable;

public abstract class AbstractService<ID extends Serializable, DTO extends IdentifierAwareDTO>
        implements CRUDService<ID, DTO>, BeanFactoryAware, CreateEntityService {

    @Setter
    BeanFactory beanFactory;

    Class<DTO> dtoClass;

    AbstractService() {
        dtoClass = ReflectionUtils.getClassGenricType(getClass(), 1);
    }

    @Override
    public DTO createEntity() {
        DTO dto = null;
        try {
            dto = dtoClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return dto;
    }

    @Override
    public DTO create(DTO dto) {
        return null;
    }

    @Override
    public DTO retrieve(ID id) {
        return null;
    }

    @Override
    public DTO retrieveByCondition(DTO condition) {
        return null;
    }

    @Override
    public boolean update(DTO dto) {
        return false;
    }

    @Override
    public boolean delete(ID id) {
        return false;
    }

    @Override
    public boolean active(ID id) {
        return false;
    }

    @Override
    public boolean deactive(ID id) {
        return false;
    }

    @Override
    public boolean exists(DTO condition) {
        return false;
    }

    @Override
    public long count(DTO condition) {
        return 0;
    }


}
