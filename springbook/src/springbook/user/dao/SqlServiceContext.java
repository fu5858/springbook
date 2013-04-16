package springbook.user.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import springbook.user.sqlservice.OxmSqlSerivce;
import springbook.user.sqlservice.SqlRegistry;
import springbook.user.sqlservice.SqlService;
import springbook.user.sqlservice.updatable.EmbeddedDbSqlRegistry;

@Configuration
public class SqlServiceContext {

	@Autowired
	SqlMapConfig sqlMapConfig;
	
	@Bean
	public DataSource embeddedDatabase(){
		return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.HSQL)
				.setName("embeddedDatabase")
				.addScript("classpath:/springbook/user/sqlservice/updatable/sqlRegistrySchema.sql")
				.build();
	}
	
	@Bean
	public SqlService sqlService(){
		OxmSqlSerivce sqlService = new OxmSqlSerivce();
		
		sqlService.setUnmarshaller(unmarshaller());
		sqlService.setSqlRegistry(sqlRegistry());
		sqlService.setSqlmap(sqlMapConfig.getSqlMapResouce());
		
		return sqlService;

	}

	@Bean
	public Unmarshaller unmarshaller(){
		Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
		
		unmarshaller.setContextPath("springbook.user.sqlservice.jaxb");
		
		return unmarshaller;
	}
	
	@Bean
	public SqlRegistry sqlRegistry(){
		EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
		
		sqlRegistry.setDataSource(embeddedDatabase());
		
		return sqlRegistry;
	}

}
