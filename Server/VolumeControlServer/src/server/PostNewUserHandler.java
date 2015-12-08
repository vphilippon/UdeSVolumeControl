package server;

import message.PostNewUserReply;
import message.PostNewUserRequest;

public class PostNewUserHandler {

	public PostNewUserReply handle(PostNewUserRequest request) {
		
		// TODO Connect to DB
		// TODO Insert user
		// TODO if bd fails, return error
		
		return new PostNewUserReply(true);
	}
}
