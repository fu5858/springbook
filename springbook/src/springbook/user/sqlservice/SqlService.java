package springbook.user.sqlservice;

public interface SqlService {
	String getSQL(String key) throws SqlRetrievalFailureException;
}
