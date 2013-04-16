package springbook.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static springbook.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserServiceImpl.MIN_RECCOUNT_FOR_GOLD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import springbook.user.dao.AppContext;
import springbook.user.dao.UserDAO;
import springbook.user.domain.Level;
import springbook.user.domain.User;

@RunWith
(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes={TestAppContext.class, AppContext.class})
@ContextConfiguration(classes=AppContext.class)
@ActiveProfiles("test")
public class UserServiceTest {

	@Autowired
	UserService userService;
	@Autowired
	UserService testUserService;

	@Autowired
	UserDAO userDAO;
	@Autowired
	DataSource dataSource;
	@Autowired
	PlatformTransactionManager transactionManager;
	@Autowired
	MailSender mailSender;
	
	@Autowired
	ApplicationContext context;
	
	List<User> users;
	
	@Before
	public void setUp(){
		users = Arrays.asList(
					new User("fu5858", "Daniel Lee", "0615", Level.GOLD, 100, Integer.MAX_VALUE, "fu5858@gmail.com"),
					new User("json8080", "Joomi Son", "0815", Level.SILVER, 98, MIN_RECCOUNT_FOR_GOLD, "json8080@hotmail.com"),
					new User("fly", "Hyungkyung Moon", "0618", Level.SILVER, 51, MIN_RECCOUNT_FOR_GOLD-1, "mhk81@hotmail.com"),
					new User("cugain", "Kieun Lee", "0123", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 18, "cugain@gmail.com"),
					new User("whiteelf", "Hyunjung Kim", "1103", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 4, "elf78@hotmail.com")
				);
	}
	
	@Test
	@DirtiesContext
	public void upgradeLevels() throws Exception {
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		MockUserDAO mockUserDAO = new MockUserDAO(users);
		userServiceImpl.setUserDAO(mockUserDAO);
		
		MockMailSender mockMailSender = new MockMailSender();
		userServiceImpl.setMailSender(mockMailSender);
		
		userServiceImpl.upgradeLevels();
		
		List<User> updated = mockUserDAO.getUpdated();
		assertThat(updated.size(), is(2));
		checkUserAndLevel(users.get(1), "json8080", Level.GOLD);
		checkUserAndLevel(users.get(3), "cugain", Level.SILVER);
		
		List<String> request = mockMailSender.getRequests();
		assertThat(request.size(), is(2));
		assertThat(request.get(1), is(users.get(3).getEmail()));
		assertThat(request.get(0), is(users.get(1).getEmail()));
	}

	private void checkUserAndLevel(User updated, String expectedID, Level expectedLevel) {
		assertThat(updated.getId(), is(expectedID));
		assertThat(updated.getLevel(), is(expectedLevel));
	}

	@Test
	public void mockUpgradeLevels() throws Exception {
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		UserDAO mockUserDAO = mock(UserDAO.class);
		when(mockUserDAO.getAll()).thenReturn(users);
		userServiceImpl.setUserDAO(mockUserDAO);
		
		MailSender mockMailSender = mock(MailSender.class);
		userServiceImpl.setMailSender(mockMailSender);
		
		userServiceImpl.upgradeLevels();
		
		verify(mockUserDAO, times(2)).update(any(User.class));
		verify(mockUserDAO, times(2)).update(any(User.class));
		verify(mockUserDAO).update(users.get(1));
		assertThat(users.get(1).getLevel(), is(Level.GOLD));
		verify(mockUserDAO).update(users.get(3));
		assertThat(users.get(3).getLevel(), is(Level.SILVER));
		
		ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
		verify(mockMailSender, times(2)).send(mailMessageArg.capture());
		List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
		assertThat(mailMessages.get(0).getTo()[0], is(users.get(1).getEmail()));
		assertThat(mailMessages.get(1).getTo()[0], is(users.get(3).getEmail()));
	}
	
	@Test
	public void add(){
		userDAO.deleteAll();
		
		User userWithLevel = users.get(0);
		User userWithoutLevel = users.get(4);
		userWithoutLevel.setLevel(null);
		
		userService.add(userWithLevel);
		userService.add(userWithoutLevel);
		
		User userWithLevelRead = userDAO.get(userWithLevel.getId());
		User userWithoutLevelRead = userDAO.get(userWithoutLevel.getId());
		
		assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
		assertThat(userWithoutLevelRead.getLevel(), is(userWithoutLevel.getLevel()));
	}
	
	@Test
	public void upgradeAllOrNothing() throws Exception {
		userDAO.deleteAll();
		
		System.out.println();
		
		for(User user : users){
			userDAO.add(user);
		}
		
		System.out.println();
		
		try{
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		}catch(TestUserServiceException e){}
		
		checkLevelUpgraded(users.get(1), false);
	}
	
	@Test(expected=TransientDataAccessResourceException.class)
	public void readOnlyTransactionAttribute(){
		testUserService.getAll();
	}

	@Test
	public void transactionSync(){
		DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
//		txDefinition.setReadOnly(true);
		TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);
		
		try{
			userService.deleteAll();
			
			userService.add(users.get(0));
			userService.add(users.get(1));
		}finally{
			transactionManager.rollback(txStatus);
		}
		
	}
	
	private void checkLevelUpgraded(User user, boolean upgraded){
		User upgradedUser = userDAO.get(user.getId());
		
		if(upgraded){
			assertThat(upgradedUser.getLevel(), is(user.getLevel().nextLevel()));
		}else{
			assertThat(upgradedUser.getLevel(), is(user.getLevel()));
		}
	}

	public static class TestUserService extends UserServiceImpl {

		private String id = "cugain";
		
		protected void upgradeLevel(User user) {
			System.out.println("user id : " + id + " = " + user.getLevel());
			if(user.getId().equals(this.id)) throw new TestUserServiceException();
			super.upgradeLevel(user);
		}
		
		public List<User> getAll(){
			for(User user : super.getAll()){
				super.update(user);
			}
			
			return null;
		}
	}
	
	public static class TestUserServiceException extends RuntimeException{

		private static final long serialVersionUID = 1L;
	}
	
	static class MockMailSender implements MailSender {
		private List<String> requests = new ArrayList<>();

		public List<String> getRequests() {
			return requests;
		}
		
		public void send(SimpleMailMessage mailMessage) throws MailException {
			requests.add(mailMessage.getTo()[0]);
		}

		public void send(SimpleMailMessage[] mailMessage) throws MailException {
		}
	}
	
	static class MockUserDAO implements UserDAO {
		
		private List<User> users;
		private List<User> updated = new ArrayList<>();
		
		private MockUserDAO(List<User> users){
			this.users = users;
		}

		public List<User> getUpdated() {
			return updated;
		}

		@Override
		public List<User> getAll() {
			return users;
		}

		@Override
		public void update(User user) {
			updated.add(user);
		}
		
		@Override
		public void add(User user) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void deleteAll() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void delete(String id) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int getCount() {
			throw new UnsupportedOperationException();
		}

		@Override
		public User get(String id) {
			throw new UnsupportedOperationException();
		}

	}


}
