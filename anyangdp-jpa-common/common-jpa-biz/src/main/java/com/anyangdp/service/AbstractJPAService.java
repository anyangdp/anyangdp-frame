package com.anyangdp.service;


import com.anyangdp.dao.BaseRepository;
import com.anyangdp.domain.AbstractPersistableEntity;
import com.anyangdp.utils.ValueUtils;
import org.springframework.util.CollectionUtils;

import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractJPAService<ID extends Serializable, DTO extends IdentifierAwareDTO, ENTITY extends AbstractPersistableEntity<ID>, DAO extends BaseRepository<ENTITY, ID>>
        extends AbstractStatelessJPAService<ID, DTO, ENTITY, DAO> {

    @Override
    protected void deleteHandler(ENTITY target) {
        target.setDeleted("1");
        target.setLastUpdatedDate(LocalDateTime.now());
        dao.save(target);
    }

    @Override
    protected void createHandler(ENTITY target) {
        target.setCreatedBy(1);
    }

    @Override
    protected void afterCreateHandler(ENTITY target) {
    }

    @Override
    protected void updateHandler(ENTITY target) {
        target.setLastUpdatedDate(LocalDateTime.now());
    }

    @Override
    protected void cleanup(ENTITY target) {
        if (!relativeFields.isEmpty()) {
            for (Field field : relativeFields.values()) {
                field.setAccessible(true);
                try {
                    if (Collection.class.isAssignableFrom(field.getType())) {
                        field.set(target, new HashSet<>());
                    } else {
                        field.set(target, null);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        dao.saveAndFlush(target);
    }

    @Override
    protected void merge(ENTITY entity) {
        if (!relativeFields.isEmpty()) {
            for (Field field : relativeFields.values()) {
                try {
                    if (Collection.class.isAssignableFrom(field.getType())) {
                        field.setAccessible(true);
                        Collection<AbstractPersistableEntity<ID>> childEntities = (Collection<AbstractPersistableEntity<ID>>) field.get(entity);

                        if (!CollectionUtils.isEmpty(childEntities)) {

                            field.setAccessible(true);

                            Set<AbstractPersistableEntity<ID>> attachedChildEntities = new HashSet<>();
                            field.set(entity, attachedChildEntities);

                            for (AbstractPersistableEntity<ID> childEntity : childEntities) {
                                if (null != childEntity && !childEntity.isNew()) {
                                    field.setAccessible(true);
                                    AbstractPersistableEntity<ID> attachedChildEntity = relativeDaos.get(field.getName()).findOne
                                            (childEntity.getId());

                                    attachedChildEntities.add(attachedChildEntity);
                                }

                                if (childEntity.isNew()) {
                                    Field[] childFields = childEntity.getClass().getDeclaredFields();

                                    for (Field childField : childFields) {
                                        if (childField.getName().equalsIgnoreCase(entity.getClass().getSimpleName())) {
                                            boolean access = childField.isAccessible();
                                            if (!access) childField.setAccessible(true);

                                            childField.set(childEntity, entity);
                                        }
                                    }
                                    attachedChildEntities.add(childEntity);
                                }
                            }
                        }
                    } else {
                        field.setAccessible(true);
                        AbstractPersistableEntity<ID> detachChildEntity = (AbstractPersistableEntity<ID>) field.get(entity);

                        if (null == detachChildEntity || detachChildEntity.isNew()) {
                            field.setAccessible(true);

                            if (null == detachChildEntity) {
                                field.set(entity, null);
                            } else {
                                if (null != entity.getId()) {
                                    detachChildEntity.setId(entity.getId());
                                }
                                field.set(entity, detachChildEntity);
                                handleChildRelation(entity, detachChildEntity);
                            }
                        } else {
                            AbstractPersistableEntity<ID> originChildEntity = null;

                            try {
                                originChildEntity = (AbstractPersistableEntity<ID>) field.getType().newInstance();
                                ValueUtils.dump(detachChildEntity, originChildEntity);
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            }
                            field.setAccessible(true);
                            field.set(entity, null);

                            AbstractPersistableEntity<ID> persistChildEntity = relativeDaos.get(field.getName()).findOne(detachChildEntity.getId
                                    ());
                            if (null != persistChildEntity) {
                                field.setAccessible(true);
                                field.set(entity, persistChildEntity);
                            } else {
                                field.setAccessible(true);
                                ValueUtils.dump(originChildEntity, persistChildEntity);
                                handleChildRelation(entity, persistChildEntity);
                                field.set(entity, persistChildEntity);
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void merge(ENTITY target, DTO dto) {
        Class clazz = dtoClass;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Transient.class)) {
                Method[] methods = clazz.getMethods();
                inner:
                for (Method method : methods) {
                    if (method.getName().contains("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1))) {
                        try {
                            Object value = method.invoke(dto);
                            if (value != null) {
                                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4), entityClass);
                                Method gg = propertyDescriptor.getWriteMethod();
                                gg.invoke(target, value);
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IntrospectionException e) {
                            e.printStackTrace();
                        }
                        break inner;
                    }
                }
            }
        }
    }

    private void handleChildRelation(ENTITY entity, AbstractPersistableEntity<ID> detachChildEntity) throws IllegalAccessException {
        for (Field childField : detachChildEntity.getClass().getDeclaredFields()) {
            if (childField.isAnnotationPresent(OneToOne.class)) {
                if (childField.getType().getSimpleName().equalsIgnoreCase(entity.getClass()
                        .getSimpleName())) {
                    childField.setAccessible(true);
                    childField.set(detachChildEntity, entity);
                }
            }
        }
    }

}
