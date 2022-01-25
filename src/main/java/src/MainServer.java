package src;

import src.TCP.server.TCPServer;
import java.io.IOException;

public class MainServer {
	public static void main(String[] args) {
		TCPServer start = new TCPServer(args[0]);
		start.launch();
	}
}