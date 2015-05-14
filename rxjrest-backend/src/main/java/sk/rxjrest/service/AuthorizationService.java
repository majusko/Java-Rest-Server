package sk.rxjrest.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sk.rxjrest.common.OAuthCommon;
import sk.rxjrest.common.PasswordHash;
import sk.rxjrest.dao.DeviceDao;
import sk.rxjrest.dao.UserDao;
import sk.rxjrest.domain.user.Device;
import sk.rxjrest.domain.user.User;
import sk.rxjrest.dto.oauth.AccessTokenDto;
import sk.rxjrest.dto.user.UserDto;
import sk.rxjrest.exception.RestErrorCode;
import sk.rxjrest.exception.RestException;


@Service
public class AuthorizationService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private DeviceDao deviceDao;

	public boolean checkClientId(String clientId) {

		if (OAuthCommon.CLIENT_ID_VALUE.equals(clientId)) {
			return true;
		}

		return false;

	}

	public boolean checkClientSecret(String secret) {

		if (OAuthCommon.CLIENT_SECRET_VALUE.equals(secret)) {
			return true;
		}

		return false;

	}

	public boolean checkAuthCode(String authCode) {

		if (OAuthCommon.REGISTRATION_CODE_VALUE.equals(authCode)) {
			return true;
		}

		return false;

	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public UserDto checkUserPass(String login, String password) throws RestException {
		
		//user don't have password now
		
//		if (login != null && password != null) {
//			final User user = userDao.getUserByLogin(login);
//			final String md5Password = DigestUtils.sha512Hex(User.SALT
//					+ password);
//
//			if (user != null) {
//				if (user.getPassword() != null) {
//					if (md5Password.equals(user.getPassword())) {
//						return new UserDto(user);
//					}
//				}
//			}
//		}

		return null;

	}

	public boolean checkRegistrationToken(String registrationToken)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		if (registrationToken != null && !registrationToken.isEmpty()) {

			registrationToken = registrationToken.replace("Bearer", "").trim();

			if (PasswordHash.validatePassword(
					OAuthCommon.REGISTRATION_CODE_VALUE, registrationToken)) {
				return true;
			}

		}

		return false;

	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public UserDto basicAuth(String accessToken) throws RestException{
		
		if (accessToken == null) {
			throw new RestException(RestErrorCode.UNAUTHORIZED_ACCESS,
					"Invalid access token");
		}
		
		accessToken = accessToken.replace("Bearer", "").trim();
		final Device device = deviceDao.getDeviceByAccessToken(accessToken);

		if (accessToken.trim().isEmpty() || device == null) {
			throw new RestException(RestErrorCode.UNAUTHORIZED_ACCESS,
					"Invalid access token");
		}

		final AccessTokenDto accessTokenDto = new AccessTokenDto(device);
		final Date currentTime = new Date();

		if (accessTokenDto.getExpireToken() == null
				|| accessTokenDto.getExpireToken().before(currentTime)) {
			throw new RestException(RestErrorCode.UNAUTHORIZED_ACCESS,
					"Expired access token");
		}

        final User user = device.getUser();
        
        user.getId();
        
        return new UserDto(user);
		
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public void checkAccessTokenByUser(String accessToken, Long userId)
			throws RestException {

		final UserDto user = basicAuth(accessToken);

		if (user == null || user.getId() != userId) {
			throw new RestException(RestErrorCode.FORBIDDEN_ACCESS,
					"User is not allowed to do this operation");
		}

	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public void checkAccessTokenByUsers(String accessToken, List<User> users)
			throws RestException {

		boolean auth = false;
		final UserDto user = basicAuth(accessToken);

		for(final User userO : users){
			if (user != null && userO != null && user.getId() == userO.getId()) {
				auth = true;
			}
		}
		
		if (!auth) {
			throw new RestException(RestErrorCode.FORBIDDEN_ACCESS,
					"User is not allowed to do this operation");
		}

	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public void checkAccessTokenByUser(String accessToken, User userA)
			throws RestException {

		final UserDto user = basicAuth(accessToken);

		if (user == null || userA == null || user.getId() != userA.getId()) {
			throw new RestException(RestErrorCode.FORBIDDEN_ACCESS,
					"User is not allowed to do this operation");
		}

	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public void checkAccessTokenByUser(String accessToken, UserDto userDto)
			throws RestException {

		final UserDto user = basicAuth(accessToken);

		if (user == null || userDto == null || user.getId() != userDto.getId()) {
			throw new RestException(RestErrorCode.FORBIDDEN_ACCESS,
					"User is not allowed to do this operation");
		}

	}

	public void updateOAuthInfo(AccessTokenDto accessToken, Long deviceId)
			throws ValidationException {

		if (accessToken != null) {

			final Device device = deviceDao.findOne(deviceId);

			device.setAccessToken(accessToken.getAccessToken());
			device.setAccessTokenExpire(accessToken.getExpireToken());
			device.setInstallationId(accessToken.getInstallationId());
			device.setRefreshToken(accessToken.getRefreshToken());

			deviceDao.update(device);

		}
	}

    /**
     * Creating new OAuth token in database
     * @param accessToken
     * @param user
     * @throws ValidationException
     */
    public void setOAuthInfo(AccessTokenDto accessToken, User user)
            throws ValidationException {

        if (accessToken != null && user != null) {

            final Device device = new Device();

            device.setAccessToken(accessToken.getAccessToken());
            device.setAccessTokenExpire(accessToken.getExpireToken());
            device.setInstallationId(accessToken.getInstallationId());
            device.setRefreshToken(accessToken.getRefreshToken());
            device.setUser(user);

            deviceDao.create(device);

        }

    }

    /**
     * Creating new OAuth token in database
     * @param accessToken
     * @param login
     * @throws ValidationException
     */
	public void setOAuthInfo(AccessTokenDto accessToken, String login)
			throws ValidationException {

        if(login != null) {
            setOAuthInfo(accessToken, userDao.getUserByLogin(login));
        }

	}

    /**
     * Creating new OAuth token in database
     * @param accessToken
     * @param userId
     * @throws ValidationException
     */
    public void setOAuthInfo(AccessTokenDto accessToken, Long userId)
            throws ValidationException {

        if(userId != null) {
            setOAuthInfo(accessToken, userDao.findOne(userId));
        }

    }

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public AccessTokenDto getOAuthInfoByLoginAndIid(String login,
			String installationId) {

		final Device device = deviceDao.getDeviceByUserLoginAndInstallationId(
				login, installationId);

		if (device == null) {
			return null;
		}

		final User user = device.getUser();

		if (user == null) {
			return null;
		}
		
		final AccessTokenDto accessToken = new AccessTokenDto(device);

		return accessToken;

	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public AccessTokenDto getOAuthInfoByRefreshToken(String refreshToken) {

		final Device device = deviceDao.getDeviceByRefreshToken(refreshToken);

		if (device == null) {
			return null;
		}

        final User user = device.getUser();

        if (user == null) {
            return null;
        }

		final AccessTokenDto accessToken = new AccessTokenDto(device);

		return accessToken;

	}

}
