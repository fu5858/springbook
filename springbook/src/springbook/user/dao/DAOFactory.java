package springbook.user.dao;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
public class DAOFactory {

	@Bean
	public UserDAO userDAO(){
		UserDAOJDBC userDAO = new UserDAOJDBC();
		userDAO.setDataSource(dataSource());
		
		return userDAO;
	}
	
	@Bean
	public ConnectionMaker connectionMaker() {
		return new DConnectionMaker();
	}
	
	@Bean
	public DataSource dataSource(){
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		
		dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost:3306/springbook");
		dataSource.setUsername("fu5858");
		dataSource.setPassword("ahrrhd");
		
		return dataSource;
	}

}
