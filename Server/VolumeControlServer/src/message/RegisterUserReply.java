package message;

import java.io.Serializable;

public class RegisterUserReply implements Serializable {
	private static final long serialVersionUID = 1479252895207703183L;
	
	private boolean _success;

	public RegisterUserReply(boolean success) {
		_success = success;
	}
	
	public boolean isSuccess() {
		return _success;
	}

}
