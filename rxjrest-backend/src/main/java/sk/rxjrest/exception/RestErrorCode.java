package sk.rxjrest.exception;

import org.springframework.http.HttpStatus;

public enum RestErrorCode {

	INVALID_INPUT("400001", "Invalid input", HttpStatus.BAD_REQUEST),
	UNAUTHORIZED_ACCESS("401001", "Unauthorized access", HttpStatus.UNAUTHORIZED),
	
	FORBIDDEN_ACCESS("403001", "Forbidden access", HttpStatus.FORBIDDEN),
	FORBIDDEN_APP("403002", "Forbidden app", HttpStatus.FORBIDDEN),
	
	USER_ALREADY_EXISTS("409002", "User already exists", HttpStatus.CONFLICT),
	USER_NOT_EXISTS("409002", "User not exists", HttpStatus.CONFLICT),
	DELETE_ERROR("409003", "It is not possible to delete item", HttpStatus.CONFLICT),
	
	RESOURCE_ALREADY_EXISTS("100422", "Resource already exists", HttpStatus.UNPROCESSABLE_ENTITY),

	RESOURCE_NOT_EXISTS("100404", "Resource not exists", HttpStatus.NOT_FOUND),
	
	INTERNAL_ERROR("500001", "Internal server error. Try again :)", HttpStatus.INTERNAL_SERVER_ERROR);

	private String mErrorCode;
	private String mErrorCodeTitle;
	private HttpStatus mStatusCode;

	private RestErrorCode(String errorCode, String errorCodeTitle,
			HttpStatus statusCode) {
		this.mErrorCode = errorCode;
		this.mErrorCodeTitle = errorCodeTitle;
		this.mStatusCode = statusCode;
	}

	public HttpStatus getStatusCode() {
		return mStatusCode;
	}

	public String getmErrorCode() {
		return mErrorCode;
	}

	public String getErrorCodeTitle() {
		return mErrorCodeTitle;
	}
}
