package springbook.user.sqlservice;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import springbook.user.dao.UserDAO;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

public class XmlSqlService implements SqlService, SqlReader, SqlRegistry {

	private SqlReader sqlReader;
	private SqlRegistry sqlRegistry;
	
	private Map<String, String> sqlMap = new HashMap<>();
	
	private String sqlmapFile;
	
	public void setSqlmapFile(String sqlmapFile) {
		this.sqlmapFile = sqlmapFile;
	}

	public void setSqlReader(SqlReader sqlReader) {
		this.sqlReader = sqlReader;
	}

	public void setSqlRegistry(SqlRegistry sqlRegistry) {
		this.sqlRegistry = sqlRegistry;
	}

	public XmlSqlService(){}

	public String findSQL(String key) throws SqlNotFoundException {
		String sql = sqlMap.get(key);
		
		if(sql == null) throw new SqlNotFoundException(key + " cannot find a sql statement!!!");
		else return sql;
	}
	
	public void registerSQL(String key, String sql){
		sqlMap.put(key, sql);
	}
	
	public void read(SqlRegistry sqlRegistry) {
		String contextPath = Sqlmap.class.getPackage().getName();
		
		try{
			JAXBContext context = JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream is = UserDAO.class.getResourceAsStream(sqlmapFile);
			Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(is);
			
			for(SqlType sql : sqlmap.getSql()){
				sqlRegistry.registerSQL(sql.getKey(), sql.getValue());
			}
		}catch(JAXBException e){
			throw new RuntimeException();
		}
	}
	
	@Override
	public String getSQL(String key) throws SqlRetrievalFailureException {
		try{
			return sqlRegistry.findSQL(key);
		}catch(SqlNotFoundException e){
			throw new SqlRetrievalFailureException(e);
		}
	}
	
	@PostConstruct
	public void loadSQL(){
		sqlReader.read(sqlRegistry);
	}

}
