package sk.rxjrest.exception;

public class RestException extends Exception {

	private static final long serialVersionUID = 1L;
	private RestErrorCode mRestErrorCode;

	public RestException(RestErrorCode restErrorCode, String message) {
		super(message);
		this.mRestErrorCode = restErrorCode;
	}

	public RestErrorCode getRestErrorCode() {
		return mRestErrorCode;
	}
}
