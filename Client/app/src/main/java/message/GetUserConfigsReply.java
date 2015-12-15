package message;

import java.util.ArrayList;

import model.VolumeConfig;

public class GetUserConfigsReply extends Reply {
	private static final long serialVersionUID = 7742995740092814903L;
	
	private ArrayList<VolumeConfig> _Volume_configs;

	public GetUserConfigsReply(boolean success, ArrayList<VolumeConfig> volumeConfigs) {
		super(success);
		_Volume_configs = volumeConfigs;
	}
	
	public ArrayList<VolumeConfig> getConfigs() {
		return _Volume_configs;
	}
	
	public void setConfigs(ArrayList<VolumeConfig> volumeConfigs) {
		_Volume_configs = volumeConfigs;
	}
}
