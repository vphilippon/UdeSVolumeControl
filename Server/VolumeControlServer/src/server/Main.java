package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import utils.ClientTCP;

public class Main {
	public static final int DEFAULT_POOL_SIZE = 10;

	public static void main(String[] args) {
		String port = args[0];
		
		System.out.println("Starting server");
		System.out.println("port: " + port);
		
		ExecutorService pool = Executors.newFixedThreadPool(DEFAULT_POOL_SIZE);
		
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(Integer.valueOf(port));
			
			while(true) {
				ClientTCP cl = new ClientTCP(ss.accept());
				cl.bind();
				
				System.out.println("Connection received");
				
				pool.execute(new MessageHandler(cl));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
