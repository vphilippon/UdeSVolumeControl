package message;

public class DeleteVolumeConfigRequest extends Request {
	private static final long serialVersionUID = -1699933659074016429L;
	
	private Integer _volumeConfigId;

	public DeleteVolumeConfigRequest(String userId, Integer volumeConfigId) {
		super(userId);
		setVolumeConfigId(volumeConfigId);
	}

	public Integer getVolumeConfigId() {
		return _volumeConfigId;
	}

	public void setVolumeConfigId(Integer _volumeConfigId) {
		this._volumeConfigId = _volumeConfigId;
	}

}
