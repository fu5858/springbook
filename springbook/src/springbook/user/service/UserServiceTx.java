package springbook.user.service;

import java.util.List;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import springbook.user.domain.User;

public class UserServiceTx implements UserService {

	UserService userService;
	PlatformTransactionManager transactionManager;
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	@Override
	public void add(User user) {
		userService.add(user);
	}

	/**
	 * Wrap the try-catch statment for the transaction
	 */
	@Override
	public void upgradeLevels() {
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try{
			userService.upgradeLevels();
			transactionManager.commit(status);
		}catch(RuntimeException e){
			System.out.println("Called rollback!!!");
			transactionManager.rollback(status);
			throw e;
		}
			
	}

	@Override
	public User get(String id) {
		return userService.get(id);
	}

	@Override
	public List<User> getAll() {
		return userService.getAll();
	}

	@Override
	public void deleteAll() {
		userService.deleteAll();
	}

	@Override
	public void delete(String id) {
		userService.delete(id);
	}

	@Override
	public void update(User user) {
		userService.update(user);
	}

}
