package server;

import message.RegisterUserReply;
import message.RegisterUserRequest;

public class RegisterUserHandler {

	public RegisterUserReply handle(RegisterUserRequest request) {
		
		// TODO Connect to DB
		// TODO Insert user
		// TODO if bd fails, return error
		
		return new RegisterUserReply(true);
	}
}
