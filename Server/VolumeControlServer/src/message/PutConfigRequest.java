package message;

import model.VolumeConfig;

public class PutConfigRequest extends Request {
	private static final long serialVersionUID = 7392193608035957632L;
	
	private VolumeConfig _updatedConfig;

	public PutConfigRequest(String userId, VolumeConfig updatedConfigs) {
		super(userId);
		_updatedConfig = updatedConfigs;
	}
	
	public VolumeConfig getUpdatedConfig() {
		return _updatedConfig;
	}

}
