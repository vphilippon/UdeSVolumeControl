package message;

public class RegisterUserReply extends Reply {
	private static final long serialVersionUID = 1479252895207703183L;
	
	public RegisterUserReply(boolean success) {
		super(success);
	}
}
