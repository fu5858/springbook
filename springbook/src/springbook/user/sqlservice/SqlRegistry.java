package springbook.user.sqlservice;

public interface SqlRegistry {
	void registerSQL(String key, String sql);
	
	String findSQL(String key) throws SqlNotFoundException;
}
