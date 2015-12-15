package message;

import java.util.ArrayList;

import model.VolumeConfig;

public class GetVolumeConfigsReply extends Reply {
	private static final long serialVersionUID = 7742995740092814903L;
	
	private ArrayList<VolumeConfig> _configs;

	public GetVolumeConfigsReply(boolean success, ArrayList<VolumeConfig> volumeConfigs) {
		super(success);
		_configs = volumeConfigs;
	}
	
	public ArrayList<VolumeConfig> getConfigs() {
		return _configs;
	}
	
	public void setConfigs(ArrayList<VolumeConfig> volumeConfigs) {
		_configs = volumeConfigs;
	}
}
