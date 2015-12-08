package message;

import model.Config;

public class PutConfigRequest extends Request {
	private static final long serialVersionUID = 7392193608035957632L;
	
	private Config _updatedConfig;

	public PutConfigRequest(String userId, Config updatedConfigs) {
		super(userId);
		_updatedConfig = updatedConfigs;
	}
	
	public Config getUpdatedConfig() {
		return _updatedConfig;
	}

}
