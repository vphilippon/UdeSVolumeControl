package message;

public class RegisterUserRequest extends Request {
	private static final long serialVersionUID = 6944803503028445195L;
	
	public RegisterUserRequest(String newId) {
		super(newId);
	}
	
}
