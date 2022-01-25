package src.TCP.client;

import src.model.Board;
import src.model.BoxPoint;

import java.io.IOException;
import java.util.Scanner;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;


import java.nio.charset.StandardCharsets;

import java.net.UnknownHostException;
import java.net.Socket;
import java.net.SocketException;

public class TCPClient {
	private boolean server = true;
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	

	public TCPClient (String port, String ip) throws IOException, SocketException {
		try {
			socket = new Socket(ip, Integer.parseInt(port));
		} catch(UnknownHostException e) {
			System.out.println("Server disable");
		}
		out = socket.getOutputStream();
		in = socket.getInputStream();
	}
	public boolean getServer() {
		return this.server;
	}
	public Board receiveObject() throws IOException, ClassNotFoundException {
		ObjectInputStream inObj = new ObjectInputStream(in);
		return (Board)inObj.readObject();
	}
	public void sendObject(Board obj) throws IOException {
		ObjectOutputStream objStream = new ObjectOutputStream(out);
		objStream.writeObject(obj);
		objStream.flush();
	}
	public String receiveData() throws IOException {
		byte[] receiveData = new byte[1024];
		in.read(receiveData);
		return (new String(receiveData, StandardCharsets.UTF_8)).trim();
	}
	public void send(String data) throws IOException {
		byte[] sendData = new byte[1024];
		sendData = data.getBytes();
		this.out.write(sendData);
		this.out.flush();
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
	public void breakConnect() {
		this.server = false;
		try {
			socket.close();
			out.close();
			in.close();
		} catch(IOException e) {
			System.out.print("ne break");
		}
		
	}

}