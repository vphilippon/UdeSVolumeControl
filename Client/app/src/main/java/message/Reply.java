package message;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class Reply implements Serializable {
	
	private boolean _success;
	
	public Reply(boolean success) {
		_success = success;
	}
	
	public boolean isSuccess() {
		return _success;
	}
	
	public void setSuccess(boolean s) {
		_success = s;
	}

}
