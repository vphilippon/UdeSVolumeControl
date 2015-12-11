package message;

public class ExistsUserRequest extends Request {
	private static final long serialVersionUID = 2154858468404781111L;

	public ExistsUserRequest(String userId) {
		super(userId);
	}
}
