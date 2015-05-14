package sk.rxjrest.service;

import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;

import sk.rxjrest.dao.UserDao;
import sk.rxjrest.domain.user.FbUser;
import sk.rxjrest.domain.user.User;
import sk.rxjrest.dto.user.UserDto;
import sk.rxjrest.email.EmailSender;
import sk.rxjrest.exception.RestErrorCode;
import sk.rxjrest.exception.RestException;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private EmailSender emailSender;

	private static final String FROM_EMAIL_ADDRESS = "rxjrest@eglu.sk";
	private static final String REGISTRATION_EMAIL_TEMPLATE = "registration";
	private static final String REGISTRATION_NAME_REPLACEMENT = "###name###";
	private static final Context ctx = new Context(LocaleContextHolder.getLocale());
	
	public User getUserByLogin(String login) {
		return userDao.getUserByLogin(login);
	}

	public User getUserById(Long id) {
		return userDao.findOne(id);
	}

	public User getUserByAccessToken(String accessToken) {
		return userDao.getUserByAccessToken(accessToken);
	}

	@Transactional(readOnly = true)
	public UserDto getUserDtoByLogin(String login) {
		return new UserDto(userDao.getUserByLogin(login));
	}

	@Transactional(readOnly = true)
	public UserDto getUserDtoById(Long id) {
		return new UserDto(userDao.findOne(id));
	}

	@Transactional(readOnly = true)
	public UserDto getUserDtoByAccessToken(String accessToken) {
		return new UserDto(userDao.getUserByAccessToken(accessToken));
	}
	
	public UserDto loginFacebookUser(String accessToken) throws RestException {

		try{
			
			final FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
			final FbUser user = facebookClient.fetchObjects(Arrays.asList("me"), FbUser.class);
			final String fbId = user.me.getId();
			final User userInDb = userDao.getUserByLogin(fbId);
			
			if(userInDb == null){

				final String lastName = user.me.getLastName();
				final String firstName = user.me.getFirstName();
				final String image = "https://graph.facebook.com/" + fbId + "/picture?width=720";
				final Date date = new Date();
				final User requestUser = new User();
				
				requestUser.setChanged(date);
				requestUser.setCreated(date);
				requestUser.setLogin(fbId);
				requestUser.setImage(image);
				requestUser.setSurname(lastName);
				requestUser.setName(firstName);
				
				final Long createdUserId = userDao.create(requestUser);
				
				//TODO assync mail sending
				
				return new UserDto(userDao.findOne(createdUserId));
				
			} else {
				return new UserDto(userInDb);
			}
			
		} catch(Exception e){
			throw new RestException(RestErrorCode.FORBIDDEN_ACCESS, "Can't login to this account.");
		}
		
	}
	
}
