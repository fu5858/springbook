package springbook.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserTest {

	User user;
	
	@Before
	public void setUp(){
		user = new User();
	}
	
	@Test
	public void upgradeLevel(){
		Level[] levels = Level.values();
		
		for(Level level : levels){
			System.out.println(level);
			if(level.nextLevel() == null) continue;
			user.setLevel(level);
			user.upgradelevel();
			assertThat(user.getLevel(), is(level.nextLevel()));
		}
	}
	
	@Test(expected=IllegalStateException.class)
	public void cannotUpgradeLeve(){
		Level[] levels = Level.values();
		
		for(Level level : levels){
			if(level.nextLevel() != null) continue;
			user.setLevel(level);
			user.upgradelevel();
		}
	}
}
