package com.anyangdp.service;

import com.anyangdp.utils.ReflectionUtils;
import lombok.Setter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import java.io.Serializable;

public abstract class AbstractService<ID extends Serializable, DTO extends IdentifierAwareDTO> implements BeanFactoryAware, PageableService<ID, DTO> {

    @Setter
    BeanFactory beanFactory;

    Class<DTO> dtoClass;

    public AbstractService() {
        dtoClass = ReflectionUtils.getClassGenricType(getClass(), 1);
    }

}
