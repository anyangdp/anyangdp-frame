package com.anyangdp.config.mutiDataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

/**
 * @Author anyang
 * @CreateTime 2018/3/23
 * @Des
 */
public class MyDataSourceTransactionManager extends DataSourceTransactionManager {
    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {

        super.doBegin(transaction, definition);
    }

}
