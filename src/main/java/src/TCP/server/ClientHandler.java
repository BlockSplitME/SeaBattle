package src.TCP.server;

import java.io.IOException;
import java.io.FileReader;
import java.util.Scanner;
import src.log.ForLogger;

import java.net.Socket;

public class ClientHandler implements Runnable {
	private ForLogger log;
	private Base base;
	private Tools tools;
	private Socket socket;
	private String room;
	
	public ClientHandler(Base base, Socket socket, ForLogger log) {
		this.base = base;
		this.socket = socket;
		this.log = log;
		this.log.info(socket.toString() + " connect to server.");
		this.tools = new Tools(base, log);
	}

	@Override
	public void run() {		
		try {
			while(!socket.isClosed()) {
				String data = tools.receive(socket.getInputStream());
				compliteCommand(data);
			}
		} catch(InterruptedException inter) {
			log.warning("Unidentified error");
		} catch(IOException excpt) {
			log.warning("Read in socket error");
		} finally {
			log.info(socket.toString() + " left...");
			if(!socket.isClosed()) {
				tools.closeSocket(socket);
			}
		}
	}
	public void compliteCommand(String data) throws IOException, InterruptedException {
		String c = tools.parseData(data);
		switch(tools.parseCommand(data)) {
			case "@quit":
				log.info(socket.toString() + " quit the game.");
				if(!this.room.equals("0")) {
					tools.destroySession(room);
				}
				tools.closeSocket(this.socket);
				break;
			case "@createRoom":
				log.info(socket.toString() + " requested to create a room " + c);
				if(tools.createGame(c, this.socket)) {
					this.room = c;
				} else {
					room = "0";
				}
				break;
			case "@connectRoom":
				log.info(socket.toString() + " requested to connect a room " + room);
				if(tools.joinGame(c, this.socket)) {
					this.room = c;
				} else {
					room = "0";
				}
				break;
			case "@ready":
				log.info("Room " + room + ": " + socket.toString() + " ready.");
				tools.ready(this.socket, this.room);
				break;
			case "@move":
				log.info("Room " + room + " " + socket.toString() + ": " + c);
				tools.move(this.socket, this.room, c);
				break;
		}
	}
}