package message;

import java.util.ArrayList;

public class GetUserConfigsReply extends Reply {
	private static final long serialVersionUID = 7742995740092814903L;
	
	private ArrayList<String> _configs; // TODO String won't do later! It got more than that!

	public GetUserConfigsReply(boolean success, ArrayList<String> configs) {
		super(success);
		_configs = configs;
	}
	
	public ArrayList<String> getConfigs() {
		return _configs;
	}
}
