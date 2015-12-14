package message;

public class ExistsUserReply extends Reply {
	private static final long serialVersionUID = -223718306519475886L;

	boolean _exists;
	
	public ExistsUserReply(boolean success, boolean exists) {
		super(success);
		_exists = exists;
	}
	
	public boolean isExisting() {
		return _exists;
	}
	
	public void setExists(boolean exists) {
		_exists = exists;
	}
}
