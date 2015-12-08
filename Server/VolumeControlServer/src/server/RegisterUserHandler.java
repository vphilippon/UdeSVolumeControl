package server;

import java.io.Serializable;

import message.RegisterUserReply;
import message.RegisterUserRequest;

public class RegisterUserHandler {

	public Serializable handle(RegisterUserRequest request) {
		
		// TODO Connect to DB
		// TODO Insert user
		// TODO if bd fails, return error
		
		return new RegisterUserReply(true);
	}
}
