package testing;

import java.io.IOException;
import message.DeleteVolumeConfigReply;
import message.DeleteVolumeConfigRequest;
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
import utils.ClientTCP;
import utils.Serializer;

public class DummyClient {

	public static void main(String[] args) {
		try {
			ClientTCP cl = new ClientTCP();
			cl.connect("localhost", 9005);
			cl.send(Serializer.serialize(new HelloWorldMessage()));
			HelloWorldMessage hw = (HelloWorldMessage) cl.receive();
			System.out.println("HelloWorld: ");
			System.out.println(hw);
			cl.close();
			
			cl = new ClientTCP();
			cl.connect("localhost", 9005);
			cl.send(Serializer.serialize(new PostNewUserRequest("TOTO3")));
			PostNewUserReply newuser = (PostNewUserReply) cl.receive();
			System.out.println("NewUser: ");
			System.out.println(newuser.isSuccess());
			cl.close();
			
			cl = new ClientTCP();
			cl.connect("localhost", 9005);
			cl.send(Serializer.serialize(new ExistsUserRequest("TOTO3")));
			ExistsUserReply exuser = (ExistsUserReply) cl.receive();
			System.out.println("ExistsUser: ");
			System.out.println(exuser.isSuccess());
			System.out.println(exuser.isExisting());
			cl.close();
			
			cl = new ClientTCP();
			cl.connect("localhost", 9005);
			cl.send(Serializer.serialize(new ExistsUserRequest("TrumpTheBright")));
			ExistsUserReply notexuser = (ExistsUserReply) cl.receive();
			System.out.println("NotExistsUser: ");
			System.out.println(notexuser.isSuccess());
			System.out.println(notexuser.isExisting());
			cl.close();
			
			cl = new ClientTCP();
			cl.connect("localhost", 9005);
			cl.send(Serializer.serialize(new PutVolumeConfigRequest("TOTO3", new VolumeConfig(null, "UdeS", 1.0, 1.0, 2, 0))));
			PutVolumeConfigReply newconf = (PutVolumeConfigReply) cl.receive();
			System.out.println("NewConf: ");
			System.out.println(newconf.isSuccess());
			cl.close();
			
			cl = new ClientTCP();
			cl.connect("localhost", 9005);
			cl.send(Serializer.serialize(new PutVolumeConfigRequest("TOTO3", new VolumeConfig(1, "UdeS", 1.0, 1.0, 3, 1))));
			PutVolumeConfigReply updateconf = (PutVolumeConfigReply) cl.receive();
			System.out.println("UpdateConf: ");
			System.out.println(updateconf.isSuccess());
			cl.close();
			
			cl = new ClientTCP();
			cl.connect("localhost", 9005);
			cl.send(Serializer.serialize(new PutVolumeConfigRequest("TOTO3", new VolumeConfig(null, "Home", 100.0, 100.0, 15, 0))));
			PutVolumeConfigReply moreconf = (PutVolumeConfigReply) cl.receive();
			System.out.println("MoreConf: ");
			System.out.println(moreconf.isSuccess());
			cl.close();
			
			cl = new ClientTCP();
			cl.connect("localhost", 9005);
			cl.send(Serializer.serialize(new GetVolumeConfigsRequest("TOTO3")));
			GetVolumeConfigsReply confs = (GetVolumeConfigsReply) cl.receive();
			System.out.println("Confs: ");
			System.out.println(confs.isSuccess());
			for (VolumeConfig c : confs.getConfigs()) {
				System.out.println(c.getName());
			}
			cl.close();
			
			cl = new ClientTCP();
			cl.connect("localhost", 9005);
			cl.send(Serializer.serialize(new DeleteVolumeConfigRequest("TOTO3", 1)));
			DeleteVolumeConfigReply deleteconf = (DeleteVolumeConfigReply) cl.receive();
			System.out.println("DeleteConf: ");
			System.out.println(deleteconf.isSuccess());
			cl.close();
			
			cl = new ClientTCP();
			cl.connect("localhost", 9005);
			cl.send(Serializer.serialize(new GetVolumeConfigsRequest("TOTO3")));
			GetVolumeConfigsReply afterdelete = (GetVolumeConfigsReply) cl.receive();
			System.out.println("AfterDelete: ");
			System.out.println(afterdelete.isSuccess());
			for (VolumeConfig c : afterdelete.getConfigs()) {
				System.out.println(c.getName());
			}
			cl.close();
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
