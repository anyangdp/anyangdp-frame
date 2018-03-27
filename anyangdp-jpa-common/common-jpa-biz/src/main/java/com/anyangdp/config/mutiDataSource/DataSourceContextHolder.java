package com.anyangdp.config.mutiDataSource;

/**
 * @Author anyang
 * @CreateTime 2018/3/23
 * @Des
 */
public class DataSourceContextHolder {
    private static final ThreadLocal<DataSourceType> contextHolder = new ThreadLocal<>();

    public static void setDataSourceType(DataSourceType dataSourceType) {
        if(dataSourceType == null){
            throw new NullPointerException();
        }
        contextHolder.set(dataSourceType);
    }

    public static DataSourceType getDataSourceType() {
        return contextHolder.get();
    }

    public static void clearDataSourceType() {
        contextHolder.remove();
    }
}
