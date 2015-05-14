package sk.rxjrest.endpoint;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

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
import sk.rxjrest.service.VisitService;

@Controller
@RequestMapping("/dashboard")
public class DashboardEndpoint extends BaseEndpoint {

	@Autowired
	private VisitService visitService;

	@Autowired
	private AuthorizationService authorizationService;

	@RequestMapping(method = RequestMethod.GET, value = "/default")
	public ResponseEntity<?> getDefaultDashBoard(
			HttpServletRequest request, HttpServletResponse response)
			throws NoSuchAlgorithmException, InvalidKeySpecException,
			SQLException {

		try {

			final String accessToken = getAccessToken(request);
			final UserDto userDto = authorizationService.basicAuth(accessToken);

			//TODO dashboard
			
			return new ResponseEntity<>(userDto, HttpStatus.OK);

		} catch (Exception e) {
			return setErrorResponse(e);
		}

	}
	
}
