package sk.rxjrest.domain.user;

import com.restfb.Facebook;
import com.restfb.types.User;

public class FbUser {

	@Facebook("me")
	public User me;

}