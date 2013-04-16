package springbook.user.sqlservice;

import java.util.HashMap;
import java.util.Map;

public class HashMapSqlRegistry implements SqlRegistry {

	private Map<String, String> sqlMap = new HashMap<String, String>();
	
	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}

	@Override
	public String findSQL(String key) throws SqlNotFoundException {
		String sql = sqlMap.get(key);
		if(sql == null) throw new SqlNotFoundException(key + " cannot find a sql statement!!!");
		else	return sql;
	}

	@Override
	public void registerSQL(String key, String sql) {
		sqlMap.put(key, sql);
	}

}
