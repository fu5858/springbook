package springbook.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import springbook.user.dao.UserDAO;
import springbook.user.domain.Level;
import springbook.user.domain.User;

@Component("userService")
public class UserServiceImpl implements UserService {
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECCOUNT_FOR_GOLD = 30;

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private MailSender mailSender;

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	/* (non-Javadoc)
	 * @see springbook.user.service.UserService#add(springbook.user.domain.User)
	 */
	@Override
	public void add(User user) {
		if(user.getLevel() == null) user.setLevel(Level.BASIC);
		userDAO.add(user);
	}

	/* (non-Javadoc)
	 * @see springbook.user.service.UserService#upgradeLevels()
	 */
	@Override
	public void upgradeLevels() {
		List<User> users = userDAO.getAll();
		for(User user : users){
			if(canUpgradeLevel(user)){
				upgradeLevel(user);
			}
		}
	}

	protected void upgradeLevel(User user) {
		user.upgradelevel();
		userDAO.update(user);
		sendUpgradeEMail(user);
	}

	private void sendUpgradeEMail(User user) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setFrom("fu5858@gmail.com");
		mailMessage.setSubject("Information for Upgrade!!!");
		mailMessage.setText("Your level is upgraded for " + user.getLevel().name());
		
		mailSender.send(mailMessage);
	}

	private boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel();
		
		switch(currentLevel){
			case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
			case SILVER: return (user.getRecommend() >= MIN_RECCOUNT_FOR_GOLD);
			case GOLD: return false;
			default: throw new IllegalArgumentException("Unknow Level : " + currentLevel);
		}
	}

	@Override
	public User get(String id) {
		return userDAO.get(id);
	}

	@Override
	public List<User> getAll() {
		return userDAO.getAll();
	}
	
	@Override
	public void delete(String id) {
		userDAO.delete(id);
	}

	@Override
	public void deleteAll() {
		userDAO.deleteAll();
	}

	@Override
	public void update(User user) {
		userDAO.update(user);
	}
}
