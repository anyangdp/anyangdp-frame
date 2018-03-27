//package com.anyangdp.config.mutiDataSource;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
//
///**
// * @Author anyang
// * @CreateTime 2018/3/23
// * @Des 暂不使用
// */
//@Slf4j
//public class RoutingDataSource extends AbstractRoutingDataSource {
//    @Override
//    protected Object determineCurrentLookupKey() {
//        if(DataSourceContextHolder.getDataSourceType() == DataSourceType.SLAVE){
//            return DataSourceType.SLAVE;
//        }
//        return DataSourceType.MASTER;
//    }
//}
