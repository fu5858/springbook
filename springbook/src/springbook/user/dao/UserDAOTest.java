package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.domain.Level;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppContext.class)
@ActiveProfiles("test")
public class UserDAOTest {
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	DefaultListableBeanFactory beanFactory;

	private User user1;  
	private User user2;
	private User user3;
	
	@Before
	public void setUP() {
		this.user1 = new User("fu5858", "Daniel Lee", "0615", Level.GOLD, 100, 40, "fu5858@hotmail.com");
		this.user2 = new User("json8080", "Joomi Son", "0815", Level.SILVER, 55, 10, "json8080@hotmail.com");
		this.user3 = new User("phoenix", "ASU", "0127", Level.BASIC, 1, 0, "phoenix@hotmail.com");
	}
	
	@Test
	public void showLevel(){
		System.out.println(Level.GOLD.intValue());
		System.out.println(Level.valueOf("GOLD"));
		System.out.println(Level.valueOf(2));
	}
	
	@Test(expected=DataAccessException.class)
	public void duplicatedAdd(){
		userDAO.deleteAll();
		userDAO.add(user1);
		userDAO.add(user1);
	}
	
	@Test
	public void addAndGet() throws ClassNotFoundException, SQLException {
		
		userDAO.deleteAll();
		assertThat(userDAO.getCount(), is(0));
		
		userDAO.add(user1);
		userDAO.add(user2);
		userDAO.add(user3);
		
		assertThat(userDAO.getCount(), is(3));
		
		User userGet1 = userDAO.get(user1.getId());
		checkSameUser(userGet1, user1);
		
		User userGet2 = userDAO.get(user2.getId());
		checkSameUser(userGet2, user2);
		
		User userGet3 = userDAO.get(user3.getId());
		checkSameUser(userGet3, user3);
	}
	
	@Test
	public void count() throws ClassNotFoundException, SQLException {
		
		userDAO.deleteAll();
		assertThat(userDAO.getCount(), is(0));
		
		userDAO.add(user1);
		assertThat(userDAO.getCount(), is(1));
		
		userDAO.add(user2);
		assertThat(userDAO.getCount(), is(2));
		
		userDAO.add(user3);
		assertThat(userDAO.getCount(), is(3));
		
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public void getUserFailure() throws ClassNotFoundException, SQLException {

		userDAO.deleteAll();
		assertThat(userDAO.getCount(), is(0));
		
		userDAO.get("unknown_id");
	}
	
	@Test
	public void getAll() throws SQLException{
		userDAO.deleteAll();
		
		List<User> users0 = userDAO.getAll();
		System.out.println(users0.size());
		assertThat(users0.size(), is(0));
		
		userDAO.add(user1);
		List<User> users1 = userDAO.getAll();
		assertThat(users1.size(), is(1));
		checkSameUser(user1, users1.get(0));
		
		userDAO.add(user2);
		List<User> users2 = userDAO.getAll();
		assertThat(users2.size(), is(2));
		checkSameUser(user1, users2.get(0));
		checkSameUser(user2, users2.get(1));
		
		userDAO.add(user3);
		List<User> users3 = userDAO.getAll();
		assertThat(users3.size(), is(3));
		checkSameUser(user1, users3.get(0));
		checkSameUser(user2, users3.get(1));
		checkSameUser(user3, users3.get(2));
	}
	
	@Test
	public void update(){
		userDAO.deleteAll();
		
		userDAO.add(user1);
		userDAO.add(user2);
		
		user1.setName("KIWON LEE");
		user1.setPassword("123456");
		user1.setLevel(Level.GOLD);
		user1.setLogin(1000);
		user1.setRecommend(999);
		
		userDAO.update(user1);
		
		User user1Update = userDAO.get(user1.getId());
		checkSameUser(user1, user1Update);
		
		User user2Update = userDAO.get(user2.getId());
		checkSameUser(user2, user2Update);
	}
	
	private void checkSameUser(User user1, User user2) {
		assertThat(user1.getId(), is(user2.getId()));
		assertThat(user1.getName(), is(user2.getName()));
		assertThat(user1.getPassword(), is(user2.getPassword()));
		assertThat(user1.getLevel(), is(user2.getLevel()));
		assertThat(user1.getLogin(), is(user2.getLogin()));
		assertThat(user1.getRecommend(), is(user2.getRecommend()));
	}
	
	@Test
	public void beans(){
		for(String name : beanFactory.getBeanDefinitionNames()){
			System.out.println(name + "\t" + beanFactory.getBean(name).getClass().getName());
		}
	}
	
}
