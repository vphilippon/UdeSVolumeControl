package message;

import java.util.ArrayList;

import model.Config;

public class GetUserConfigsReply extends Reply {
	private static final long serialVersionUID = 7742995740092814903L;
	
	private ArrayList<Config> _configs;

	public GetUserConfigsReply(boolean success, ArrayList<Config> configs) {
		super(success);
		_configs = configs;
	}
	
	public ArrayList<Config> getConfigs() {
		return _configs;
	}
	
	public void setConfigs(ArrayList<Config> configs) {
		_configs = configs;
	}
}
