package sk.rxjrest.endpoint;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import sk.rxjrest.dto.common.ErrorDto;
import sk.rxjrest.exception.ForbiddenException;
import sk.rxjrest.exception.RestErrorCode;
import sk.rxjrest.exception.RestException;

public class BaseEndpoint {

	public static final String HEADER_AUTHORIZATION = "Authorization";
	public static final String BEARER = "Bearer";
	public static final String BASIC = "Basic";
	
	public BaseEndpoint() {

	}

	public String getAccessToken(HttpServletRequest request) {

		String accessToken = request.getHeader(HEADER_AUTHORIZATION);

		if (accessToken != null) {
			accessToken = accessToken.replace(BEARER, "");
			accessToken = accessToken.replace(BASIC, "");
			accessToken = accessToken.trim();
		}

		return accessToken;

	}

	public ResponseEntity<?> setErrorResponse(Exception e) {

		if(e instanceof ForbiddenException){
			e = new RestException(RestErrorCode.FORBIDDEN_ACCESS, e.getMessage());
		}
		
		if (e instanceof RestException) {
			final HttpHeaders headers = new HttpHeaders();
			final RestErrorCode restErrorCode = ((RestException) e)
					.getRestErrorCode();
			if (restErrorCode == RestErrorCode.UNAUTHORIZED_ACCESS) {
				headers.add("WWW-Authenticate", "Bearer realm=\"example\"");
			}
			final ResponseEntity<?> response = new ResponseEntity<ErrorDto>(
					new ErrorDto(restErrorCode.getErrorCodeTitle(),
							e.getMessage(), restErrorCode.getmErrorCode()),
					headers, restErrorCode.getStatusCode());

			return response;
		} else {
			final ResponseEntity<ErrorDto> response = new ResponseEntity<ErrorDto>(
					new ErrorDto("All wrong error", e.getMessage(), ""),
					HttpStatus.INTERNAL_SERVER_ERROR);

			return response;
		}
	}

}
