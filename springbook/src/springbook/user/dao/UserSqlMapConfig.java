package springbook.user.dao;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


public class UserSqlMapConfig implements SqlMapConfig {

	@Override
	public Resource getSqlMapResouce() {
		return new ClassPathResource("sqlmap.xml", UserDAO.class);
	}

}
