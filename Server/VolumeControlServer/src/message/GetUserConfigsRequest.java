package message;

public class GetUserConfigsRequest extends Request {
	private static final long serialVersionUID = -9179620374969240477L;
	
	public GetUserConfigsRequest(String userId) {
		super(userId);
	}
}
