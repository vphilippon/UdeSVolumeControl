package server;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;

import message.GetUserConfigsRequest;
import message.HelloWorldMessage;
import message.PostNewUserRequest;
import message.PutConfigRequest;
import utils.ClientUDP;
import utils.Serializer;

public class MessageHandler implements Runnable {

	DatagramPacket _receivedData;
	
	public MessageHandler(DatagramPacket data) {
		_receivedData = data;
	}

	@Override
	public void run() {
		try {
			Object receivedMessage = Serializer.deserialize(_receivedData.getData());
			System.out.println(receivedMessage);
			
			Serializable reply = null;
			
			if(receivedMessage instanceof HelloWorldMessage) {
				System.out.println("Got a HelloWorldMessage, how sweet!");
				System.out.println("Replying :)");
				reply = new HelloWorldMessage();
			} else if(receivedMessage instanceof PostNewUserRequest) {
				System.out.println("User registration request");
				reply = new PostNewUserHandler().handle((PostNewUserRequest) receivedMessage);
			} else if(receivedMessage instanceof GetUserConfigsRequest) {
				System.out.println("Get user configs request");
				reply = new GetUserConfigsHandler().handle((GetUserConfigsRequest) receivedMessage);
			} else if(receivedMessage instanceof PutConfigRequest) {
				System.out.println("Put config request");
				reply = new PutConfigHandler().handle((PutConfigRequest) receivedMessage);
			} else {
				System.out.println("Unknown message type received");
			}

			if(reply != null) {
				ClientUDP cl = new ClientUDP();
				cl.connect(_receivedData.getSocketAddress());
				cl.send(Serializer.serialize(reply));
				System.out.println("REPLY SENT");
			}
			
		} catch (ClassNotFoundException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
