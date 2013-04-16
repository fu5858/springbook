package springbook.user.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import springbook.user.domain.User;

@Transactional
public interface UserService {

	/**
	 * Add a new user
	 * @param user
	 */
	public void add(User user);

	/**
	 * Get a user information from the id;
	 * @param id
	 * @return
	 */
	@Transactional(readOnly=true)
	public User get(String id);
	
	/**
	 * Get all users
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<User> getAll();
	
	/**
	 * Delete all user information
	 */
	public void deleteAll();
	
	/**
	 * Delete user information from the id
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * Update user information
	 * @param user
	 */
	public void update(User user);
	
	/**
	 * Upgrade of a level for the user
	 */
	public void upgradeLevels();

}