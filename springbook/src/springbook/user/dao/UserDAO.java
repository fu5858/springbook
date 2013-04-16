package springbook.user.dao;

import java.util.List;

import springbook.user.domain.User;

public interface UserDAO {

	/**
	 * Add to new user
	 * @param user
	 */
	public void add(User user);

	/**
	 * Delete all users
	 */
	public void deleteAll();

	/**
	 * Delete a user with id
	 */
	public void delete(String id);

	/**
	 * Total number of users
	 * @return total number
	 */
	public int getCount();

	/**
	 * Search a user with id
	 * @param id
	 * @return User Object
	 */
	public User get(String id);

	/**
	 * Get all of users from the users table
	 * @return
	 */
	public List<User> getAll();

	/**
	 * Update user's info
	 * @param user
	 */
	public void update(User user);
	

}