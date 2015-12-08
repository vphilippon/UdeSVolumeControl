package server;

import message.GetUserConfigsReply;
import message.GetUserConfigsRequest;

public class GetUserConfigsHandler {
	
	public GetUserConfigsReply handle(GetUserConfigsRequest request) {
		
		// TODO Connect to DB
		// TODO fetch configs for user
		// TODO if none or bd fails, return error
		
		return new GetUserConfigsReply(false, null);
	}

}
