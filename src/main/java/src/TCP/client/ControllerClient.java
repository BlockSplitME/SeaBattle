package src.TCP.client;

import src.TCP.MyException;
import src.view.*;
import src.model.*;
import java.io.IOException;

import src.log.ForLogger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ControllerClient extends Thread {
	private ForLogger log;
	private TCPClient player;
	private Board myBoard;
	private Board enemyBoard;
	private ViewPreparationForBattle playerBoardView;
	private ViewBattle playerView;
	private boolean active = true;
//-----------------------------------------Запросы------------------------------------------
	//Создание клиента
	public ControllerClient(int mod, int room, ForLogger log) throws ClassNotFoundException, IOException, MyException {
		this.log =log;
		if(room == 0) {
			throw new MyException("Wrong number");
		}
		this.player = new TCPClient("1234","localhost");
		switch(mod) {
			case 1: //host
				player.send("@createRoom " + room);
				log.info("Request for create send!");
				if(player.receiveData().equals("@error")) {
					this.breakConnect();
					throw new MyException("The room exist.");
				}
				break;
			case 2://connect
				player.send("@connectRoom " + room);
				log.info("Request for connect send!");
				String answer = player.receiveData();
				if(answer.equals("@error")) {
					this.breakConnect();
					throw new MyException("The room not found.");
				} else if(answer.equals("@busy")) {
					this.breakConnect();
					throw new MyException("Game is already running.");
				}
				break;
		}
		this.start();
		this.createEmptyField();
		playerBoardView = new ViewPreparationForBattle(this);
	}

	public Board getBoard() {
		return myBoard;
	}

	public Board getEnemyBoard() {
		return enemyBoard;
	}

	public void createEmptyField() {
		myBoard = new Board();
		enemyBoard = new Board();
		ModelBoard.createEmptyField(getBoard());
		ModelBoard.createEmptyField(getEnemyBoard());
	}

	public void createRandomField() {
		ModelBoard.createRandomField(getBoard());
	}

	public void addShip(Ships ship) {
		ModelBoard.addShip(ship, getBoard());
	}

	public void removeShip(Ships ship) {
		ModelBoard.removeShip(ship, getBoard());
	}

	public boolean ready() throws IOException, ClassNotFoundException {
		if(myBoard.getAllShips().size() != 10) {
			return false;
		}
		log.info("Request about START.");
		player.send("@ready i");
		log.info("Send board");
		player.sendObject(getBoard());
		log.info("Waiting start...");			
		return true;
	}

	public void startGame() throws IOException {
			log.info("Start game!");
			playerBoardView.dispose();
			playerBoardView = null;
			playerView = new ViewBattle(this);
			//this.start();			
	}

	public void breakConnect() throws IOException {
		this.active = false;
		player.send("@quit 1");
		log.info("Request quit.");
	}

	public void closeBattle(boolean mode) {
		if(playerView != null) {
			playerView.exitRival(mode);
		}
		if(playerBoardView != null) {
			playerBoardView.exitRival(mode);
		}
	}
//-------------------------------Listener----------------------------------
	@Override
	public void run() {
		log.info("Start listener");
		while(player.getServer() && active) {
			try {			
				this.handler(player.receiveData());
			} catch(IOException e) {
				log.info("Handler close.");
				break;
			}
		}
		log.info("End listener");
		
	}

	public void handler(String data) throws IOException {
		String c = TCPClient.parseData(data);
		switch(TCPClient.parseCommand(data)) {
			case "@start":
				this.startGame();	
				break;
			case "@quit":
				player.breakConnect();
				if(!active) {
					closeBattle(false);

				} else {
					closeBattle(true);
					active = false;
				}
				break;
			case "@myBoard":
				changeBoard(c, getBoard());
				playerView.repaintMy();
				break;	
			case "@enemyBoard":	
				changeBoard(c, getEnemyBoard());
				playerView.repaintEnemy();
				break;
			case "@fatalMy":
				myBoard.setMyFieldForView(Board.parseFrom(c));	
				playerView.repaintMy();
				break;
			case "@fatalEnemy":
				enemyBoard.setMyFieldForView(Board.parseFrom(c));	
				playerView.repaintEnemy();
				break;
			case "@end":
				endGame(c);
				break;
			default:
				break;
			
		}
	}

//----------------------------------------------Game-----------------------------
	public void changeBoard(String data, Board board) {
		int x = Integer.parseInt(data.substring(0,1));
		int y = Integer.parseInt(data.substring(2,3));
		String name = data.substring(4,7);
		BoxPoint box = createBox(name, x, y);
		ModelBoard.setBox(board.getMyField(), box);
		if(box.getPicture() == Defolt.BANGHIT) {
			forAnnimation(x,y,board);
		}
		if(data.charAt(8) == 'm') {
			myBoard.setMyFieldForView(Board.parseFrom(data.substring(11)));	
		} else if(data.charAt(8) == 'e') {
			enemyBoard.setMyFieldForView(Board.parseFrom(data.substring(11)));	
		}
	}

	public boolean doMove(int x, int y) {//поле кто ходит
		try {
			player.send("@move "+x + "-" + y);
			return true;
		} catch(IOException e) {
			return false;
		}
	}
		
	private BoxPoint createBox(String name, int x, int y) {
		System.out.println(name);
		if(name.equals("hit")) {
			return (new BoxPoint(x*Defolt.IMAGE_SIZE,y*Defolt.IMAGE_SIZE,Defolt.BANGHIT,true));
		}
		return (new BoxPoint(x*Defolt.IMAGE_SIZE,y*Defolt.IMAGE_SIZE,Defolt.BANGMISS,true));
	}

	public void endGame(String data){
		new WinMenu(data,this);
	}
	
//--------------------------------------------Textures-----------------------------------
	public void forAnnimation(int x,int y, Board board) { 
		Annimation an = new Annimation(x, y, board, playerView);
		an.start();
	}

	public static Image getImage(String nameImage) {
        String fileName;
        String extension = ".png";
        fileName = "/home/evgeniy/suai/kur/client/src/main/java/src/view/textures/" + nameImage.toLowerCase() + extension; 
        ImageIcon icon = new ImageIcon(fileName);
        return icon.getImage();
    }
//---------------------------------------------Menu--------------------------------------
    public static void createFrame(JFrame view, int w, int h,boolean center){
  		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		if(center) {
			int locationX = (screenSize.width - w) / 2;
			int locationY = (screenSize.height - h) / 2;
			view.setBounds(locationX, locationY, w, h);
    	}
		view.setLayout(null);
		view.setResizable(false);
		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		view.setIconImage(ControllerClient.getImage("icon"));
		view.setVisible(true);
    }

    public static void changeCountInButton(ViewPreparationForBattle view) {
        view.getChoosePanel().setNameOneDeck(4 - view.getControllerClient().getBoard().getOneDeckShip().size());
        view.getChoosePanel().setNameTwoDeck(3 - view.getControllerClient().getBoard().getTwoDeckShip().size());
        view.getChoosePanel().setNameThreeDeck(2 - view.getControllerClient().getBoard().getThreeDeckShip().size());       
        view.getChoosePanel().setNameFourDeck(1 - view.getControllerClient().getBoard().getFourDeckShip().size());    
    }
}