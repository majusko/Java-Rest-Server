package sk.rxjrest.endpoint;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import sk.rxjrest.common.OAuthCommon;
import sk.rxjrest.common.PasswordHash;
import sk.rxjrest.dto.oauth.AccessTokenDto;
import sk.rxjrest.dto.user.UserDto;
import sk.rxjrest.exception.RestErrorCode;
import sk.rxjrest.exception.RestException;
import sk.rxjrest.service.AuthorizationService;
import sk.rxjrest.service.UserService;

@Controller
@RequestMapping("/token")
public class TokenEndpoint extends BaseEndpoint {

	@Autowired(required = true)
	private AuthorizationService authorizationService;

	@Autowired(required = true)
	private UserService userService;

	@RequestMapping(method = RequestMethod.POST, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> authorize(
			@RequestParam(value = OAuthCommon.INSTALLATION_ID, required = false) String installationId,
			@RequestParam(value = OAuthCommon.CLIENT_ID, required = true) String clientId,
			@RequestParam(value = OAuthCommon.CLIENT_SECRET, required = true) String clientSecret,
			@RequestParam(value = OAuthCommon.GRAND_TYPE, required = true) String grandType,
			@RequestParam(value = OAuthCommon.USERNAME, required = false) String username,
			@RequestParam(value = OAuthCommon.PASSWORD, required = false) String password,
			@RequestParam(value = OAuthCommon.APPVERSION, required = false) String appVersion,
			@RequestParam(value = OAuthCommon.REGISTRATION_CODE, required = false) String registrationCode,
			@RequestParam(value = OAuthCommon.FACEBOOK_ACCESS_TOKEN, required = false) String facebookAccessToken,
			@RequestParam(value = OAuthCommon.GOOGLE_ACCESS_TOKEN, required = false) String googleAccessToken,
			@RequestParam(value = OAuthCommon.REFRESH_TOKEN, required = false) String refreshToken,
			@RequestParam(value = OAuthCommon.CODE, required = false) String code,
			HttpServletRequest request, HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException,
			ValidationException, NoSuchAlgorithmException,
			InvalidKeySpecException {

		final Date currentTimestamp = new Date();
		final long hoursInMillis = 2L * 60L * 1000L;
		final Date expireTimestamp = new Date(currentTimestamp.getTime()
				+ hoursInMillis);
		final AccessTokenDto oAuthInfo = new AccessTokenDto();

		String newAccessToken = UUID.randomUUID().toString();
		String newRefreshToken = UUID.randomUUID().toString();

		oAuthInfo.setAccessToken(newAccessToken);
		oAuthInfo.setRefreshToken(newRefreshToken);
		oAuthInfo.setExpireToken(expireTimestamp);

		if (installationId != null) {
			oAuthInfo.setInstallationId(installationId);
		}

		try {

			// check if clientid is valid
			if (!authorizationService.checkClientId(clientId)) {
				throw new RestException(RestErrorCode.UNAUTHORIZED_ACCESS,
						"Invalid client Id code");
			}

			// check if client_secret is valid
			if (!authorizationService.checkClientSecret(clientSecret)) {
				throw new RestException(RestErrorCode.UNAUTHORIZED_ACCESS,
						"Invalid secret Id code");
			}

			// do checking for different grant types
			if (grandType.equals(OAuthCommon.GRAND_TYPE_REGISTRATION)) {

				if (!authorizationService.checkAuthCode(registrationCode)) {
					throw new RestException(RestErrorCode.UNAUTHORIZED_ACCESS,
							"Invalid registration code");
				}

				oAuthInfo.setAccessToken(PasswordHash
						.createHash(OAuthCommon.REGISTRATION_CODE_VALUE));
				oAuthInfo.setRefreshToken("");

			} else if (grandType.equals(OAuthCommon.GRAND_TYPE_PASSWORD)) {

				final UserDto user = authorizationService.checkUserPass(username, password);
				
				if (user == null || user.getId() == null) {
					throw new RestException(RestErrorCode.UNAUTHORIZED_ACCESS,
							"Invalid password");
				}

				if (installationId == null) {
					throw new RestException(RestErrorCode.UNAUTHORIZED_ACCESS,
							"Invalid installationId");
				}

				final AccessTokenDto tempAccessTokenDto = authorizationService
						.getOAuthInfoByLoginAndIid(user.getLogin(), installationId);

				if (tempAccessTokenDto != null) {
					authorizationService.updateOAuthInfo(oAuthInfo,
							tempAccessTokenDto.getAccessTokenId());
				} else {
					authorizationService.setOAuthInfo(oAuthInfo, user.getLogin());
				}

			} else if (grandType.equals(OAuthCommon.REFRESH_TOKEN)) {

				if (refreshToken == null) {
					throw new RestException(RestErrorCode.UNAUTHORIZED_ACCESS,
							"Invalid refreshToken");
				}

				final AccessTokenDto accessTokenDto = authorizationService
						.getOAuthInfoByRefreshToken(refreshToken);

				if (accessTokenDto == null) {
					throw new RestException(RestErrorCode.UNAUTHORIZED_ACCESS,
							"Invalid refreshToken");
				}

				authorizationService.updateOAuthInfo(oAuthInfo,
						accessTokenDto.getAccessTokenId());

			} else if (grandType.equals(OAuthCommon.GRAND_TYPE_FACEBOOK)) {

				if (facebookAccessToken == null) {
					throw new RestException(RestErrorCode.FORBIDDEN_ACCESS,
							"Invalid facebook token");
				}
				
				if (installationId == null) {
					throw new RestException(RestErrorCode.FORBIDDEN_ACCESS,
							"Invalid installationId");
				}
				
				final UserDto user = userService.loginFacebookUser(facebookAccessToken);
				final AccessTokenDto tempAccessTokenDto = authorizationService
						.getOAuthInfoByLoginAndIid(user.getLogin(), installationId);

				if (tempAccessTokenDto != null) {
					authorizationService.updateOAuthInfo(oAuthInfo,
							tempAccessTokenDto.getAccessTokenId());
				} else {
					authorizationService.setOAuthInfo(oAuthInfo, user.getId());
				}

			} else {
				throw new RestException(RestErrorCode.FORBIDDEN_ACCESS,
					"Unknown grand type");
			}

			return new ResponseEntity<AccessTokenDto>(oAuthInfo, HttpStatus.OK);

		} catch (Exception e) {
			return setErrorResponse(e);
		}

	}

}