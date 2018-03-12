package com.anyangdp.factory;

public class MapperEntityFactory implements EntityFactory {
    @Override
    public <T> T newInstance(Class<T> entityClass) {
        try {
            return entityClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            return null;
        }
    }
}
