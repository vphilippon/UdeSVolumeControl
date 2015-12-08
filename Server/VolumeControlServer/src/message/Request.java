package message;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class Request implements Serializable {
	
	private String _id;
	
	public Request(String id) {
		_id = id;
	}
	
	public String getId() {
		return _id;
	}

}
