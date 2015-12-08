package testing;

import java.io.IOException;
import java.net.DatagramPacket;

import message.GetUserConfigsReply;
import message.GetUserConfigsRequest;
import message.PutConfigReply;
import message.PutConfigRequest;
import model.Config;
import utils.ClientUDP;
import utils.Serializer;

public class DummyClient {

	public static void main(String[] args) {
		try {
			ClientUDP cl = new ClientUDP(1000); // 1 sec timeout
			cl.connect("localhost", 9005);
			
			System.out.println("SENDING");
			//cl.send(Serializer.serialize(new HelloWorldMessage()));
			//cl.send(Serializer.serialize(new PostNewUserRequest("TOTO3")));
			//cl.send(Serializer.serialize(new PutConfigRequest("TOTO3", new Config(null, 1, 1, 2.0, 5))));
			//cl.send(Serializer.serialize(new PutConfigRequest("TOTO3", new Config(1, 1, 1, 2.0, 6))));
			cl.send(Serializer.serialize(new GetUserConfigsRequest("TOTO3")));
			
			DatagramPacket rep = cl.receive();
			//HelloWorldMessage mess = (HelloWorldMessage) Serializer.deserialize(rep.getData());
			//PostNewUserReply mess = (PostNewUserReply) Serializer.deserialize(rep.getData());
			//PutConfigReply mess = (PutConfigReply) Serializer.deserialize(rep.getData());
			GetUserConfigsReply mess = (GetUserConfigsReply) Serializer.deserialize(rep.getData());
			
			System.out.println("GOT REPLY");
			System.out.println(mess);
			System.out.println(mess.isSuccess());
			System.out.println(mess.getConfigs());
			for (Config c : mess.getConfigs()) {
				System.out.println(c.getVolumeMain());
			}
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
