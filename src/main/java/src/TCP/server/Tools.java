package src.TCP.server;

import src.model.Board;
import src.model.BoxPoint;
import src.model.Defolt;

import java.io.IOException;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.net.Socket;

import src.log.ForLogger;

public class Tools {
	private static ForLogger log;
	private Base base;
	private static int maxPacketSize = 1024;
	private static int maxNameSize = 128;

	public Tools(Base base, ForLogger log) {
		this.base = base;
		this.log = log;
	}
	
	public static void closeSocket(Socket socket) {
		try {
			socket.close();
		} catch(IOException e) {
			log.warning("Client " + socket.toString() + " already leave.");
		} catch(NullPointerException npe) {
			log.warning("Client " + socket.toString() + " not exist.");		
		}
	}
	public void destroySession(String room) {
		base.findSession(Integer.parseInt(room)).myDestroy();
		if(base.deleteSession(Integer.parseInt(room))) {
			log.info("Session " + room + " deleted.");
		} else {
			log.info("Session " + room + " no exist.");
		}
	}
	public static void playerQuit(Socket socket) {
		try {
			send("@quit 1", socket.getOutputStream());
			log.info("Client " + socket.toString() + " received quit...");
		} catch(IOException e) {
			log.warning("Client " + socket.toString() + " already leave...");	
		} catch(NullPointerException npe) {
			log.warning("Client " + socket.toString() + " not exist...");		
		}
	}
//-------------------Обработка команд
	//---------------Создание-------------
	public boolean createGame (String room, Socket socket) {
		try {
			if(base.newSession(Integer.parseInt(room), socket)) {
				log.info("Room " + room + " create.");
				send("@ok",socket.getOutputStream());
				return true;
			} else {
				send("@error", socket.getOutputStream());
				log.info("Room " + room + " just exist.");
				return false;
			}
		} catch(IOException excpt) {
			log.warning("Client " + socket.toString() + " not exist.");
			return false;
		}
	}	
	public boolean joinGame (String room, Socket socket) {
		try {
			int ans = base.connectSession(Integer.parseInt(room), socket);
			if(ans == 1) {
				log.info(socket.toString() + " connect to " + room + ".");				
				send("@ok",socket.getOutputStream());
				return true;
			} else if(ans == -1) {
				log.info(socket.toString() + " not connect to " + room + ".");				
				send("@busy", socket.getOutputStream());
				return false;
			}
			send("@error", socket.getOutputStream());
			return false;
		} catch (IOException excpt) {
			log.warning("Client " + socket.toString() + " not exist.");
			return false;
		}
	}
	//---------------Запуск-------------

	public static void startGame(Socket p1, Socket p2, Board f1, Board f2) {
		try {
			send("@start 1", p1.getOutputStream());
			send("@start 1", p2.getOutputStream());
			log.info("Announce the start both!");
		} catch(IOException excpt) {
			log.warning("Unidentified exception");
		}
	}
	public static Board requestBoard(Socket socket) throws IOException, ClassNotFoundException, InterruptedException {
		log.info("Request board from " + socket.toString());
		return receiveBoard(socket.getInputStream());
	}
	public void ready(Socket socket, String room) throws IOException, InterruptedException {
		base.findSession(Integer.parseInt(room)).playerReady(socket);
	}
	//---------------Игра-------------
	public void move(Socket socket, String room, String xy) throws IOException, InterruptedException {
		int x = Integer.parseInt(xy.substring(0,1));
		int y = Integer.parseInt(xy.substring(2));
		base.findSession(Integer.parseInt(room)).playerMove(socket, x, y);
	}
	public static void resMove(Socket p, BoxPoint box, Socket rivals, Board rivalsBoard, Board defRival) throws IOException, InterruptedException {		
		if(box == null) {
			send("@noturn e", p.getOutputStream());
			log.info("Not your's move " + p.toString() + " ...");
		} else if(box.getPicture() == Defolt.BANGHIT) {
			int x = box.getX()/Defolt.IMAGE_SIZE;
			int y = box.getY()/Defolt.IMAGE_SIZE;
			log.info(p.toString() + " hit " + x + "|" + y);
			send("@myBoard " + x + " " + y + " hit "+ "my " + Board.parseTo(rivalsBoard.getMyField(), 0, 0, 10, 10), rivals.getOutputStream());
			send("@enemyBoard " + x + " " + y + " hit " + "en " + Board.parseTo(defRival.getMyField(), 0, 0, 10, 10), p.getOutputStream());			
		} else if(box.getPicture() == Defolt.BANGMISS){
			int x = box.getX()/Defolt.IMAGE_SIZE;
			int y = box.getY()/Defolt.IMAGE_SIZE;
			log.info(p.toString() + " miss " + x + "|" + y);	
			send("@myBoard " + x + " " + y + " miss n", rivals.getOutputStream());
			send("@enemyBoard " + x + " " + y + " miss n", p.getOutputStream());
		} 
	}
	public static void endGame(Socket p, Socket rival) throws IOException {
		send("@end You are win!!", p.getOutputStream());
		send("@end You are lose(", rival.getOutputStream());
	}
//-------------------Обработка объектов
	public static Board receiveBoard(InputStream player) throws IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(player);
		return (Board)in.readObject();
	}
//------------------Отправка объектов
	public static void sendBoard(Board obj, OutputStream out) throws IOException {
		ObjectOutputStream objStream = new ObjectOutputStream(out);
		objStream.writeObject(obj);
		objStream.flush();
	}
//-------------------Обработка данных

	public static String receive(InputStream in) throws IOException {
		byte[] receiveData = new byte[maxNameSize + maxPacketSize];
		try {
		in.read(receiveData);
			return (new String(receiveData, StandardCharsets.UTF_8)).trim();
		} catch (IOException e) {
			return "null";
		}
	}
	public static String parseData(String data) {
		if(data.charAt(0) != '@') {
			return data;
		} else {
			return data.substring(data.indexOf(' ')+1);
		}
	}

	public static String parseCommand(String data) {
		if(data.charAt(0) == '@') {
			return data.substring(0, data.indexOf(' '));
		} else {
			return "";
		}
	}
//--------------------Отправка данных
	public static void send(String data, OutputStream out) throws IOException {
		byte[] sendData = new byte[maxNameSize + maxPacketSize];
		sendData = data.getBytes();
		out.write(sendData);
		out.flush(); 
	}
}