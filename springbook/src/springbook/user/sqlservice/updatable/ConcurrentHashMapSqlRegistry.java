package springbook.user.sqlservice.updatable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import springbook.user.sqlservice.SqlNotFoundException;
import springbook.user.sqlservice.SqlUpdateFailureException;
import springbook.user.sqlservice.UpdatableSqlRegistry;

public class ConcurrentHashMapSqlRegistry implements UpdatableSqlRegistry {
	
	private Map<String, String> sqlMap = new ConcurrentHashMap<>();

	@Override
	public void registerSQL(String key, String sql) {
		sqlMap.put(key, sql);
	}

	@Override
	public String findSQL(String key) throws SqlNotFoundException {
		String sql = sqlMap.get(key);
		if(sql == null) throw new SqlNotFoundException(key + " cannot find a sql statement!!!");
		else	return sql;
	}

	@Override
	public void updateSql(String key, String sql)
			throws SqlUpdateFailureException {
		if(sqlMap.get(key) == null) 
			throw new SqlUpdateFailureException(key + " cannot find a sql statement!!!");
		
		sqlMap.put(key, sql);

	}

	@Override
	public void updateSql(Map<String, String> sqlmap)
			throws SqlUpdateFailureException {
		for(Map.Entry<String, String> entry : sqlmap.entrySet()){
			updateSql(entry.getKey(), entry.getValue());
		}
	}

}
