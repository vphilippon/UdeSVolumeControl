package testing;

import java.io.IOException;
import java.net.DatagramPacket;

import message.ExistsUserReply;
import message.ExistsUserRequest;
import message.GetVolumeConfigsReply;
import message.GetVolumeConfigsRequest;
import message.HelloWorldMessage;
import message.PostNewUserReply;
import message.PostNewUserRequest;
import message.PutVolumeConfigReply;
import message.PutVolumeConfigRequest;
import model.VolumeConfig;
import utils.ClientUDP;
import utils.Serializer;

public class DummyClient {

	public static void main(String[] args) {
		try {
			ClientUDP cl = new ClientUDP(1000); // 1 sec timeout
			cl.connect("localhost", 9005);
			
			DatagramPacket data;
			
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
			
			cl.send(Serializer.serialize(new ExistsUserRequest("TOTO3")));
			data = cl.receive();
			ExistsUserReply exuser = (ExistsUserReply) Serializer.deserialize(data.getData());
			System.out.println("ExistsUser: ");
			System.out.println(exuser.isSuccess());
			System.out.println(exuser.isExisting());
			
			cl.send(Serializer.serialize(new ExistsUserRequest("TrumpTheBright")));
			data = cl.receive();
			ExistsUserReply notexuser = (ExistsUserReply) Serializer.deserialize(data.getData());
			System.out.println("NotExistsUser: ");
			System.out.println(notexuser.isSuccess());
			System.out.println(notexuser.isExisting());
			
			cl.send(Serializer.serialize(new PutVolumeConfigRequest("TOTO3", new VolumeConfig(null, "UdeS", 1.0, 1.0, 2, 0))));
			data = cl.receive();
			PutVolumeConfigReply newconf = (PutVolumeConfigReply) Serializer.deserialize(data.getData());
			System.out.println("NewConf: ");
			System.out.println(newconf.isSuccess());
			
			cl.send(Serializer.serialize(new PutVolumeConfigRequest("TOTO3", new VolumeConfig(1, "UdeS", 1.0, 1.0, 3, 1))));
			data = cl.receive();
			PutVolumeConfigReply updateconf = (PutVolumeConfigReply) Serializer.deserialize(data.getData());
			System.out.println("UpdateConf: ");
			System.out.println(updateconf.isSuccess());
			
			cl.send(Serializer.serialize(new PutVolumeConfigRequest("TOTO3", new VolumeConfig(null, "Another", 2.0, 2.0, 3, 1))));
			data = cl.receive();
			PutVolumeConfigReply moreconf = (PutVolumeConfigReply) Serializer.deserialize(data.getData());
			System.out.println("MoreConf: ");
			System.out.println(moreconf.isSuccess());
			
			cl.send(Serializer.serialize(new GetVolumeConfigsRequest("TOTO3")));
			data = cl.receive();
			GetVolumeConfigsReply confs = (GetVolumeConfigsReply) Serializer.deserialize(data.getData());
			System.out.println("Confs: ");
			System.out.println(confs.isSuccess());
			for (VolumeConfig c : confs.getConfigs()) {
				System.out.println(c.getName());
			}
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
