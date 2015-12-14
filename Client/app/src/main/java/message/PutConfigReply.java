package message;

public class PutConfigReply extends Reply {
	private static final long serialVersionUID = -488308106858982459L;

	public PutConfigReply(boolean success) {
		super(success);
	}

}
