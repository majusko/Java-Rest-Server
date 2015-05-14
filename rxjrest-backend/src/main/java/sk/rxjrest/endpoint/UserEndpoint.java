package sk.rxjrest.endpoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import sk.rxjrest.dto.user.UserDto;
import sk.rxjrest.service.AuthorizationService;
import sk.rxjrest.service.UserService;

@Controller
@RequestMapping("/user")
public class UserEndpoint extends BaseEndpoint {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthorizationService authorizationService;

	@RequestMapping(method = RequestMethod.GET, value = "/token")
	public ResponseEntity<?> getUserByAccessToken(
			HttpServletRequest request, HttpServletResponse response) {

		try {

			final String accessToken = getAccessToken(request);
			final UserDto userDto = authorizationService.basicAuth(accessToken);

			return new ResponseEntity<>(userDto, HttpStatus.OK);
			
		} catch (Exception e) {
			return setErrorResponse(e);
		}

	}

}
