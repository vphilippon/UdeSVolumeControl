package message;

import java.io.Serializable;

public class RegisterUserRequest implements Serializable {
	private static final long serialVersionUID = 6944803503028445195L;
	
	private String _newUsername;
	
	public RegisterUserRequest(String newUsername) {
		_newUsername = newUsername;
	}
	
	public String getNewUsername() {
		return _newUsername;
	}

}
