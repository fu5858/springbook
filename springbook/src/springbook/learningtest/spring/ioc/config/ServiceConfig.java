package springbook.learningtest.spring.ioc.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
public class ServiceConfig {

	@Bean
	public DataSource dataSource(){
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		
		dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost:3306/springbookwithtest");
		dataSource.setUsername("fu5858");
		dataSource.setPassword("ahrrhd");
		
		return dataSource;
	}
}

