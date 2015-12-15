package message;

import model.VolumeConfig;

public class PutConfigRequest extends Request {
	private static final long serialVersionUID = 7392193608035957632L;
	
	private VolumeConfig _updatedVolumeConfig;

	public PutConfigRequest(String userId, VolumeConfig updatedConfigs) {
		super(userId);
		_updatedVolumeConfig = updatedConfigs;
	}
	
	public VolumeConfig getUpdatedConfig() {
		return _updatedVolumeConfig;
	}

}
