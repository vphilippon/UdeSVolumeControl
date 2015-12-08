package testing;

import java.io.IOException;
import java.net.DatagramPacket;
import message.PostNewUserReply;
import message.PostNewUserRequest;
import utils.ClientUDP;
import utils.Serializer;

public class DummyClient {

	public static void main(String[] args) {
		try {
			ClientUDP cl = new ClientUDP();
			cl.connect("localhost", 9005);
			
			System.out.println("SENDING");
			//cl.send(Serializer.serialize(new HelloWorldMessage()));
			cl.send(Serializer.serialize(new PostNewUserRequest("TOTO1")));
			
			DatagramPacket rep = cl.receive();
			//HelloWorldMessage mess = (HelloWorldMessage) Serializer.deserialize(rep.getData());
			PostNewUserReply mess = (PostNewUserReply) Serializer.deserialize(rep.getData());
			
			System.out.println("GOT REPLY");
			System.out.println(mess);
			System.out.println(mess.isSuccess());
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
