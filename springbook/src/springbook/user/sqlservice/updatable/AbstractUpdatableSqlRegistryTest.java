package springbook.user.sqlservice.updatable;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import springbook.user.sqlservice.SqlNotFoundException;
import springbook.user.sqlservice.SqlUpdateFailureException;
import springbook.user.sqlservice.UpdatableSqlRegistry;

public abstract class AbstractUpdatableSqlRegistryTest {

	UpdatableSqlRegistry sqlRegistry;
	
	abstract protected UpdatableSqlRegistry createUpdatableSqlRegistry();
	
	@Before
	public void setUp(){
		sqlRegistry = createUpdatableSqlRegistry();
		
		sqlRegistry.registerSQL("KEY1", "SQL1");
		sqlRegistry.registerSQL("KEY2", "SQL2");
		sqlRegistry.registerSQL("KEY3", "SQL3");
	}
	
	@Test
	public void find(){
		checkFindResult("SQL1", "SQL2", "SQL3");
	}

	protected void checkFindResult(String expected1, String expected2, String expected3) {
		assertThat(sqlRegistry.findSQL("KEY1"), is(expected1));
		assertThat(sqlRegistry.findSQL("KEY2"), is(expected2));
		assertThat(sqlRegistry.findSQL("KEY3"), is(expected3));
	}
	
	@Test(expected=SqlNotFoundException.class)
	public void unknownKey(){
		sqlRegistry.findSQL("SQL999@#$");
	}
	
	@Test
	public void updateSingle(){
		sqlRegistry.updateSql("KEY2", "Modified2");
		checkFindResult("SQL1", "Modified2", "SQL3");
	}
	
	@Test
	public void dupdateMuti(){
		Map<String, String> sqlmap = new HashMap<String, String>();
		sqlmap.put("KEY1", "Modified1");
		sqlmap.put("KEY3", "Modified3");
		
		sqlRegistry.updateSql(sqlmap);
		checkFindResult("Modified1", "SQL2", "Modified3");
	}
	
	@Test(expected=SqlUpdateFailureException.class)
	public void updateWithNoExistingKey(){
		sqlRegistry.updateSql("SQL999!@#$", "Modified2");
	}
}
