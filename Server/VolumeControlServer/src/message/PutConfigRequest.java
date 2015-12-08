package message;

public class PutConfigRequest extends Request {
	private static final long serialVersionUID = 7392193608035957632L;
	
	private String _updatedConfig;

	public PutConfigRequest(String userId, String updatedConfigs) {
		super(userId);
	}
	
	public String getUpdatedConfig() {
		return _updatedConfig;
	}

}
