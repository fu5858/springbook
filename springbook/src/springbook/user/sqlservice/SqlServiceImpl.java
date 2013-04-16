package springbook.user.sqlservice;

import java.util.Map;

public class SqlServiceImpl implements SqlService {

	private Map<String, String> sqlMap;

	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}

	@Override
	public String getSQL(String key) throws SqlRetrievalFailureException {
		String query = sqlMap.get(key);
		
		if(query == null){
			throw new SqlRetrievalFailureException(key + " cannot find a sql statement!!!");
		}else{
			return query;
		}
	}

}
