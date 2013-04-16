package springbook.user.sqlservice;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;

import springbook.user.dao.UserDAO;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

public class OxmSqlSerivce implements SqlService {

	private class OxmSqlReader implements SqlReader {
		
		private Unmarshaller unmarshaller;
		private final static String DEFAULT_SQLMAP_FILE = "sqlmap.xml";
		private Resource sqlmap = new ClassPathResource("sqlmap.xml", UserDAO.class);
		
		public void setUnmarshaller(Unmarshaller unmarshaller) {
			this.unmarshaller = unmarshaller;
		}

		public void setSqlmap(Resource sqlmap) {
			this.sqlmap = sqlmap;
		}

		@Override
		public void read(SqlRegistry sqlRegistry) {
			try{
				Source source = new StreamSource(sqlmap.getInputStream());
				Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(source);
				
				for(SqlType sql : sqlmap.getSql()){
					sqlRegistry.registerSQL(sql.getKey(), sql.getValue());
				}
			}catch(IOException e){
				throw new IllegalArgumentException(sqlmap.getFilename() + " cannot get.", e);
			}
		}

	}

	private final OxmSqlReader oxmSqlReader = new OxmSqlReader();
	private final BaseSqlService baseSqlService = new BaseSqlService();
	
	private SqlRegistry sqlRegistry = new HashMapSqlRegistry();
	
	public void setSqlRegistry(SqlRegistry sqlRegistry) {
		this.sqlRegistry = sqlRegistry;
	}
	
	public void setUnmarshaller(Unmarshaller unmarshaller){
		oxmSqlReader.setUnmarshaller(unmarshaller);
	}
	
	public void setSqlmap(Resource sqlmap){
		oxmSqlReader.setSqlmap(sqlmap);
	}
	
	@PostConstruct
	public void loadSQL() {
		baseSqlService.setSqlReader(oxmSqlReader);
		baseSqlService.setSqlRegistry(sqlRegistry);
		
		baseSqlService.loadSQL();
	}
	
	@Override
	public String getSQL(String key) throws SqlRetrievalFailureException {
		return baseSqlService.getSQL(key);
	}

}
