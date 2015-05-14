package sk.rxjrest.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sk.rxjrest.dao.generic.GenericHibernateDao;
import sk.rxjrest.domain.user.Device;

@Repository
public class DeviceDao extends GenericHibernateDao<Device> {
	
	public DeviceDao() {
        super(Device.class);
    }
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true, propagation=Propagation.REQUIRED)
	public Device getDeviceByAccessToken(String accessToken) {
		
		final List<Device> list = (List<Device>) getCurrentSession()
				.createQuery("from Device where accessToken = ?")
				.setParameter(0, accessToken)
				.list();
			
		return listToOneResult(list);

	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true, propagation=Propagation.REQUIRED)
	public Device getDeviceByUserIdAndInstallationId(Long userId, String installationId) {

		final List<Device> list = (List<Device>) 
				getCurrentSession()
				.createQuery("from Device where userId = ? and installationId = ? ")
				.setParameter(0, userId)
				.setParameter(1, installationId)
				.list();
			
		return listToOneResult(list);

	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true, propagation=Propagation.REQUIRED)
	public Device getDeviceByUserLoginAndInstallationId(String login, String installationId) {
		
		final List<Device> list = (List<Device>) 
				getCurrentSession()
				.createQuery("select d from Device d where d.user.login = ? "
				+ " and d.installationId = ? and d.user.inactiveFlag = 0 ")
				.setParameter(0, login)
				.setParameter(1, installationId)
				.list();
			
		return listToOneResult(list);

	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true, propagation=Propagation.REQUIRED)
	public Device getDeviceByRefreshToken(String refreshToken) {
		
		final List<Device> list = (List<Device>) 
				getCurrentSession()
				.createQuery("from Device where refreshToken = ?")
				.setParameter(0, refreshToken)
				.list();
			
		return listToOneResult(list);

	}

}
