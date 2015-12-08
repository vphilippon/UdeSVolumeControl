package testing;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;

import message.HelloWorldMessage;
import utils.ClientUDP;
import utils.Serializer;

public class DummyClient {

	public static void main(String[] args) {
		try {
			ClientUDP cl = new ClientUDP();
			cl.connect("localhost", 9005);
			
			System.out.println("SENDING");
			cl.send(Serializer.serialize(new HelloWorldMessage()));
			
			DatagramPacket rep = cl.receive();
			
			HelloWorldMessage mess = (HelloWorldMessage) Serializer.deserialize(rep.getData());
			
			System.out.println("GOT REPLY");
			System.out.println(mess);
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
