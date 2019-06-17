package com.ca.ms.cup.common.db.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

//2018-1-15增加配置信息加密组件

//
//@Configuration
public class DataSourceConfig {

    @Value("${spring.druid.username}")
    private String username;

	@Value("${spring.druid.password}")
    private String password;


    @Bean(name = "myDataSource")
    @Primary   //默认数据源
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource mysqlDataSource() {

    	return DataSourceBuilder.create()
    			.type(DataSource.class)
    			.username(username)
    			.password(password)
    			.build();
    }
}
