package message;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class Request implements Serializable {
	
	private String _userId;
	
	public Request(String userId) {
		_userId = userId;
	}
	
	public String getUserId() {
		return _userId;
	}

}
