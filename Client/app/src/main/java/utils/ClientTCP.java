package utils;

import java.io.*;
import java.net.*;

public class ClientTCP {
	private Socket _socket = null;
	private DataInputStream _in = null;
    private DataOutputStream _out = null;
	
	public ClientTCP() throws IOException {
		this(new Socket());
    }
	
	public ClientTCP(Socket s) throws IOException {
		_socket = s;
    }

    public void bind() throws IOException {
    	_in = new DataInputStream(_socket.getInputStream());
		_out = new DataOutputStream(_socket.getOutputStream());
    }
    
    public void connect(String hostname, int port) throws IOException {
    	connect(new InetSocketAddress(hostname, port));
    }
    
    public void connect(SocketAddress addr) throws IOException {
    	_socket.connect(addr);
    	_in = new DataInputStream(_socket.getInputStream());
		_out = new DataOutputStream(_socket.getOutputStream());
    }
    
    public void send(byte[] msg) throws IOException {
    	_out.write(msg);
    }
    
    public Object receive() throws IOException, ClassNotFoundException {
    	ObjectInputStream obj_ss = new ObjectInputStream(_in);
    	return  obj_ss.readObject();
    }

    public void close() throws IOException {
        _socket.close();
    }

}
