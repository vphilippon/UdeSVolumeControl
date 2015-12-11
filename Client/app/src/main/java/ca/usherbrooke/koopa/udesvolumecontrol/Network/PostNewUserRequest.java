package ca.usherbrooke.koopa.udesvolumecontrol.Network;

public class PostNewUserRequest extends Request {
	private static final long serialVersionUID = 6944803503028445195L;
	
	public PostNewUserRequest(String newUserId) {
		super(newUserId);
	}
	
}
