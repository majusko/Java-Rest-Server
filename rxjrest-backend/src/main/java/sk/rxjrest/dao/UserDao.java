package sk.rxjrest.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sk.rxjrest.dao.generic.GenericHibernateDao;
import sk.rxjrest.domain.user.User;

@Repository
public class UserDao extends GenericHibernateDao<User> {
	
	public UserDao() {
        super(User.class);
    }

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true, propagation=Propagation.REQUIRED)
	public User getUserByLogin(String login) {
		
		final List<User> list = (List<User>) 
				getCurrentSession()
				.createQuery("from User where login = ? and inactiveFlag = 0")
				.setParameter(0, login)
				.list();
			
		return listToOneResult(list);

	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true, propagation=Propagation.REQUIRED)
	public User getUserByAccessToken(String accessToken) {
		final List<User> list = (List<User>)
				getCurrentSession()
				.createQuery("select u from User u "
						+ " left join u.devices d where "
						+ " d.accessToken = ? and "
						+ " u.inactiveFlag = 0 ")
				.setParameter(0, accessToken)
				.list();

		return listToOneResult(list);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true, propagation=Propagation.REQUIRED)
	public User getUserByRefreshToken(String refreshToken) {

		final List<User> list = (List<User>)
				getCurrentSession()
				.createQuery("select u from User u "
						+ " left join u.devices d where "
						+ " d.refreshToken = ? and "
						+ " u.inactiveFlag = 0 ")
				.setParameter(0, refreshToken)
				.list();

		return listToOneResult(list);

	}
	
}
