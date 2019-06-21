package com.ca.ms.cup;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;


@SpringBootApplication
@ImportResource("classpath:/spring/spring-config.xml")
@MapperScan("com.ca.ms.cup.dao")
public class CupApplication {

	public static void main(String[] args) {
		SpringApplication.run(CupApplication.class, args);
	}
//	@Bean
//	@ConfigurationProperties(prefix = "db")
//	public DataSource dateSource() {
//		DruidDataSource dataSource = new DruidDataSource();
//		return dataSource;
//	}
}
