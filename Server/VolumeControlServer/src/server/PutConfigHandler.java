package server;

import message.PutConfigReply;
import message.PutConfigRequest;

public class PutConfigHandler {
	
	public PutConfigReply handle(PutConfigRequest request) {
		
		// TODO Check for ConfigID. if it's null, it's a new one, and we have to insert. else, it's an update.
		
		// TODO Connect to DB
		// TODO insert/update
		// TODO if bd fails, return error
		
		return new PutConfigReply(true);
	}

}
