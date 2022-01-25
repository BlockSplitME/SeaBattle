package src.TCP.server;

import java.lang.NullPointerException;
import java.io.IOException;
import java.net.Socket;


import java.util.concurrent.ConcurrentHashMap;
import java.util.Collection;

import java.io.OutputStream;

public class Base {
	private static ConcurrentHashMap <String, OutputStream> clients;
	private static ConcurrentHashMap <Integer, Session> sessions;
	public Base() {
		clients = new ConcurrentHashMap <String, OutputStream>();
		sessions = new ConcurrentHashMap <Integer, Session>(); 
	}
//--------------Сессии
	public boolean newSession(int room, Socket socket){
		if(!this.isContainSession(room)) {
			Session ses = new Session(socket, room);
			sessions.put(room, ses);
			return true;
		}
		return false;
	}
	public int connectSession(int room, Socket socket) {
		if(this.isContainSession(room)) {
			if(sessions.get(room).connect(socket)) {
				return 1;
			}
			return -1;
		}
		return -2;
	}  
	public Session findSession(int room) {
		return sessions.get(room);
	}
	public boolean isContainSession(int room) {
		return sessions.containsKey(room);
	}
	public boolean deleteSession(int room) {
		if(this.isContainSession(room)) {
			sessions.remove(room);
			return true;
		}
		return false;
	}
//------------Клиенты
	public OutputStream getOutputStream(String name) {
		OutputStream out = clients.get(name);
		if (out == null) {
			throw new NullPointerException("No client");
		}
		return out;
	}

	public boolean set(String name, OutputStream out) {
		if(this.isContain(name)) {
			return false;
		} else {
			clients.put(name, out);
			return true;
		}
	}

	public Collection<String> getNames() {
		return clients.keySet();
	}

	public void delete(String name) {
		clients.remove(name);
	}

	public boolean rename(String oldName, String newName) {
		if (this.isContain(newName)) {
			return false;			
		}
		OutputStream out = clients.remove(oldName);
		clients.put(newName, out);
		return true;
	}

	public void disableAll() {
		for (OutputStream stream : clients.values()) {
			close(stream);
		}
	}

	private void close(OutputStream stream) {
		try{
			stream.close();
		} catch(IOException e) {

		}
	}

	public boolean alone() {
		if(clients.size() <= 1) {
			return true;
		}
		return false;
	}

	public boolean isContain(String name) {
		return clients.containsKey(name);
	}
}