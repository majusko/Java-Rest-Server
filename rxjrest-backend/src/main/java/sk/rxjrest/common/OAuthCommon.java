package sk.rxjrest.common;

public class OAuthCommon {
	
    public static final String CLIENT_ID_VALUE = "ApRYgau9juyt5433g4g5t6XYHP7ZsPNZ";
    public static final String CLIENT_SECRET_VALUE = "gtrrbvfe345t56h65g443g45hh5g45g46";
    public static final String AUTHORIZATION_CODE_VALUE = "dHC7SQTFagqc5efHdmyMqs9G3nBWeTveywgT";
    public static final String REGISTRATION_CODE_VALUE = "D6kFeyncmFjZ4UsexhybLq3SYpSgsYq8hrG6";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    
    //URL_PARAMS
    public static final String EMAIL = "email";
    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRET = "client_secret";
    public static final String GRAND_TYPE = "grant_type";
    public static final String INSTALLATION_ID = "installation_id";
    //GRAND TYPE = registration
    //potrebujes - registration_code, client id, client secret
    public static final String GRAND_TYPE_REGISTRATION = "registration";
    public static final String REGISTRATION_CODE = "registration_code";
    //GRAND TYPE = password
    //potrebujes - client id, client secret, username, pass, installation_d
    public static final String GRAND_TYPE_PASSWORD = "password";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String APPVERSION = "appversion";
    //GRAND TYPE = facebook
    //potrebujes - client id, client secret, facebook_access_token, installation_id
    public static final String GRAND_TYPE_FACEBOOK = "facebook";
    public static final String FACEBOOK_ACCESS_TOKEN = "facebook_access_token";
    //GRAND TYPE = google
    //potrebujes - client id, client secret, google_access_token, installation_id
    public static final String GRAND_TYPE_GOOGLE = "google";
    public static final String GOOGLE_ACCESS_TOKEN = "google_access_token";
    //GRAND TYPE = refresh_token
    //potrebujes - client id, client secret, refresh_token
    public static final String GRAND_TYPE_REFRESH_TOKEN = "refresh_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    //GRAND TYPE = authorization_code
    //potrebujes - client id, client secret, authorization_code, code
    public static final String GRAND_TYPE_AUTHORIZATION_CODE = "authorization_code";
    public static final String CODE = "code";

}