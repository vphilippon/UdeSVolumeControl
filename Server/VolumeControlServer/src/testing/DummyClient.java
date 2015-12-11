package testing;

import java.io.IOException;
import java.net.DatagramPacket;

import message.GetUserConfigsReply;
import message.GetUserConfigsRequest;
import message.HelloWorldMessage;
import message.PostNewUserReply;
import message.PostNewUserRequest;
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
			
			DatagramPacket data;
			
			System.out.println("SENDING");
			cl.send(Serializer.serialize(new HelloWorldMessage()));
			data = cl.receive();
			HelloWorldMessage hw = (HelloWorldMessage) Serializer.deserialize(data.getData());
			System.out.println("HelloWorld: ");
			System.out.println(hw);
			
			cl.send(Serializer.serialize(new PostNewUserRequest("TOTO3")));
			data = cl.receive();
			PostNewUserReply newuser = (PostNewUserReply) Serializer.deserialize(data.getData());
			System.out.println("NewUser: ");
			System.out.println(newuser.isSuccess());
			
			cl.send(Serializer.serialize(new PutConfigRequest("TOTO3", new Config(null, "UdeS", 1.0, 1.0, 2, 5, 6))));
			data = cl.receive();
			PutConfigReply newconf = (PutConfigReply) Serializer.deserialize(data.getData());
			System.out.println("NewConf: ");
			System.out.println(newconf.isSuccess());
			
			cl.send(Serializer.serialize(new PutConfigRequest("TOTO3", new Config(1, "UdeS", 1.0, 1.0, 3, 0, 1))));
			data = cl.receive();
			PutConfigReply updateconf = (PutConfigReply) Serializer.deserialize(data.getData());
			System.out.println("UpdateConf: ");
			System.out.println(updateconf.isSuccess());
			
			cl.send(Serializer.serialize(new GetUserConfigsRequest("TOTO3")));
			data = cl.receive();
			GetUserConfigsReply confs = (GetUserConfigsReply) Serializer.deserialize(data.getData());
			System.out.println("Confs: ");
			System.out.println(confs.isSuccess());
			for (Config c : confs.getConfigs()) {
				System.out.println(c.getConfigName());
			}
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
