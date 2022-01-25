package src.TCP.server;


import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;

import java.net.Socket;
import java.net.ServerSocket;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import src.log.ForLogger;

public class TCPServer {
	private ForLogger log;
	private ServerSocket socket;
	private ExecutorService executor;
	private Base base;

	public TCPServer(String port) {
		try {
			log = new ForLogger("Server");
			this.socket = new ServerSocket(Integer.parseInt(port));
			this.base = new Base();
			this.executor = Executors.newCachedThreadPool();
		} catch(IOException excpt) {
			System.out.println("Error socket.");
		} catch(NullPointerException npe) {
			System.out.println("Error logger.");
		}
	}

	public void launch() {
		try {
			log.info("Server start.");
			chanell();
		} catch (IOException e) {
			e.getMessage();
			e.printStackTrace();
		} finally {
			log.info("Server close.");
			base.disableAll();
			executor.shutdown();
		}

	}

	private void chanell() throws IOException {
		long i = 1;
		while(true) {
			Socket client = socket.accept();
			ClientHandler addClient = new ClientHandler(base, client, log);
			executor.submit(addClient);
		}
	}
}
