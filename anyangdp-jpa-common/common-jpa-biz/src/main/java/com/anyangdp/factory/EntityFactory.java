package com.anyangdp.factory;

public interface EntityFactory {

    <T> T newInstance(Class<T> entityClass);
}
