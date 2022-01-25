package src.TCP.server;
import src.model.*;
import src.controller.*;


import java.lang.NullPointerException;
import java.io.IOException;
import java.net.Socket;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Collection;

import java.io.OutputStream;

public class Session {
	private Socket player1;
	private Socket player2;
	private int name;
	private boolean p1 = false;
	private boolean p2 = false;
	private boolean full = false;
	private Controller game;
	
	public Session (Socket player1, int room) {
		this.player1 = player1;
		this.name = room;
		game = new Controller();
	}
	public boolean connect(Socket player2) {
		if(!full) {
			this.player2 = player2;
			full = true;
			return true;
		} 
		return false;
	}
	public OutputStream getOutPlayerOne() throws IOException {
		return player1.getOutputStream();
	}
	public OutputStream getOutPlayerTwo() throws IOException {
		return player2.getOutputStream();
	}
	public void myDestroy() {
		Tools.playerQuit(player1);
		Tools.playerQuit(player2);
		Tools.closeSocket(player1);
		Tools.closeSocket(player2);
	}
	public void playerReady(Socket socket) throws InterruptedException {
		if(player1.equals(socket)) {
			p1 = true;
			try{
				this.game.connectFirstPlayer(Tools.requestBoard(player1));
			} catch (IOException e) {

			} catch (ClassNotFoundException e){}
		} else {
			p2 = true;
			try {
				this.game.connectSecondPlayer(Tools.requestBoard(player2));
			} catch (IOException e) {

			} catch (ClassNotFoundException e){}
			
		}
		if(p2 && p1) {
			Tools.startGame(player1, player2, game.getFirst(), game.getSecond());
		}
	}
	public void playerMove(Socket socket, int x, int y) throws IOException, InterruptedException {
		synchronized(game) {

			if(player1.equals(socket)) {
				BoxPoint box = game.doMove(x,y,game.getFirst());			
				Tools.resMove(player1, box, player2, game.getSecond(),defendField(game.getSecond()));
			} else {
				BoxPoint box = game.doMove(x,y,game.getSecond());
				Tools.resMove(player2, box, player1, game.getFirst(), defendField(game.getFirst()));
			}
			String winner = game.isWin();
			if(winner.equals("first")) {
				Tools.endGame(player1, player2);
			} else if(winner.equals("second")) {
				Tools.endGame(player2, player1);
			}
		}

	}
	public Board defendField(Board board) {
		Board tmp = new Board();
		ModelBoard.createEmptyField(tmp);
		for(int i = 0; i < Defolt.ROWS; i++) {
			for(int j = 0; j < Defolt.COLUMNS; j++) {
				if(board.getMyField()[i][j].getPicture() == Defolt.BANGMISS) {
					ModelBoard.setBox(tmp.getMyField(), board.getMyField()[i][j]);
				}
			}
		}
		return tmp;
	}
}