package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.exception.DuplicateUserIdException;
import springbook.user.sqlservice.SqlService;

@Repository
public class UserDAOJDBC implements UserDAO {

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private SqlService sqlService;
	
	public void setSqlService(SqlService sqlService) {
		this.sqlService = sqlService;
	}

	@Autowired
	public void setDataSource(DataSource dataSource){
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	/* (non-Javadoc)
	 * @see springbook.user.dao.UserDAO#add(springbook.user.domain.User)
	 */
	@Override
	public void add(final User user) throws DuplicateUserIdException {
		System.out.println("Add New User for " + user.getId() + " : " + user.getName() + " : " + user.getLevel());
		
		jdbcTemplate.update(sqlService.getSQL("add"), user.getId(), user.getName(), user.getPassword()
				, user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail());
	}
	
	/* (non-Javadoc)
	 * @see springbook.user.dao.UserDAO#deleteAll()
	 */
	@Override
	public void deleteAll() {
		System.out.println("Delete All of the users");
		
		jdbcTemplate.update(sqlService.getSQL("deleteAll"));
	}
	
	/* (non-Javadoc)
	 * @see springbook.user.dao.UserDAO#delete(java.lang.String)
	 */
	@Override
	public void delete(String id) {
		String deleteQuery = "delete from users11 where id = ? ";
		
		jdbcTemplate.update(sqlService.getSQL("delete"), id);
	}
	
	/* (non-Javadoc)
	 * @see springbook.user.dao.UserDAO#getCount()
	 */
	@Override
	public int getCount() {
		
		String countingQuery = "select count(*) cnt from users11 ";
		
		return jdbcTemplate.queryForInt(sqlService.getSQL("getCount"));
	}
	
	/* (non-Javadoc)
	 * @see springbook.user.dao.UserDAO#get(java.lang.String)
	 */
	@Override
	public User get(String id) {
		String selectQuery = "select id, name, password, level, login, recommend, email from users11 where id = ? ";
		
		return jdbcTemplate.queryForObject(sqlService.getSQL("get"), new Object[] {id}, userMapper);
	}
	
	/* (non-Javadoc)
	 * @see springbook.user.dao.UserDAO#getAll()
	 */
	@Override
	public List<User> getAll() {
//		System.out.println("Get all user's info");
//		System.out.println(sqlService.getSQL("getAll"));
		String selectQuery = " select id, name, password, level, login, recommend, email from users11 ";
		
		return jdbcTemplate.query(sqlService.getSQL("getAll"), userMapper);
	}
	
	/* (non-Javadoc)
	 * @see springbook.user.dao.UserDAO#update(springbook.user.domain.User)
	 */
	@Override
	public void update(User user) {
//		System.out.println("Update user's info for " + user.getId() + " : " + user.getName() + " : " + user.getLevel());
		
		String updateQuery = "update users11 set " +
				"name = ?, password = ?, level = ?, login = ?, recommend = ?, email = ? " +
				"where id = ?";
		jdbcTemplate.update(sqlService.getSQL("update"), user.getName(), user.getPassword(), user.getLevel().intValue()
				, user.getLogin(), user.getRecommend(), user.getEmail(), user.getId());
	}
	
	private RowMapper<User> userMapper = new RowMapper<User>(){
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			user.setLevel(Level.valueOf(rs.getInt("level")));
			user.setLogin(rs.getInt("login"));
			user.setRecommend(rs.getInt("recommend"));
			user.setEmail(rs.getString("email"));
			return user;
		}
	};
}
