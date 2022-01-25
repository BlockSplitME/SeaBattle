package src.view.panels;

import src.view.*;
import src.TCP.client.ControllerClient;
import src.model.*;
import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyField extends JPanel{
	private ChoosePanel choosePanel;
	private ViewPreparationForBattle view;
	private ViewBattle viewBattle;
	private Board board;
//расстановка
	public MyField(ViewPreparationForBattle view){
		this.view = view;
		this.choosePanel = this.view.getChoosePanel();
		this.board = view.getControllerClient().getBoard();
		this.setBounds(40, 40, 400, 400);
		this.setBackground(Color.blue);
		this.addMouseListener(new ActionMouse());
	}
	public ViewPreparationForBattle getBoard() {
		return this.view;
	}
	public void setBoard(Board board){
		this.board = board;
	}
//игра
	public MyField(ViewBattle viewBattle){
		this.viewBattle = viewBattle;
		this.board = this.viewBattle.getControllerClient().getBoard();
		this.setBounds(40, 40, 400, 400);
		repaint();
	}
	@Override
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
        BoxPoint[][] matrix = board.getMyField(); //изменил доску на матрицу
        for (int i = 0; i < Defolt.COLUMNS; i++) {
            for (int j = 0; j < Defolt.ROWS; j++) {
                BoxPoint box = matrix[i][j]; 
                if (box == null) continue;
                g.drawImage(ControllerClient.getImage(box.getPicture().name()), box.getX(), box.getY(),this);
            }
        }
    }
    public void callChange(){
    	view.getControllerClient().changeCountInButton(view);
    }
	private class ActionMouse extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e){
			int x = (e.getX()/ Defolt.IMAGE_SIZE) ;
			int y = (e.getY()/ Defolt.IMAGE_SIZE) ;

			int countDeck = choosePanel.getCountDeck();
			int placement = choosePanel.getPlacement();

			Ships ship;
			if(e.getButton() == MouseEvent.BUTTON1){
				if(countDeck > 0 && countDeck <=4 && placement != 0 ){
					ship = new Ships(countDeck);
					if(placement == 1) ship.touchVert();
					if(ship.createShip(x,y, ship.isVert(), view.getControllerClient().getBoard().getMyField())) {
						view.getControllerClient().addShip(ship);
					}
				}
				board = view.getControllerClient().getBoard();
				repaint();
			}
			else if(e.getButton() == MouseEvent.BUTTON3){
				ArrayList<Ships> allShips = view.getControllerClient().getBoard().getAllShips();
				for(Ships removeShip : allShips) {
					for(BoxPoint box: removeShip.getBoxesOfShip()){
						if(x*Defolt.IMAGE_SIZE == box.getX() && y*Defolt.IMAGE_SIZE == box.getY()){
							view.getControllerClient().removeShip(removeShip);
							break;
						}
					}
				}
				board = view.getControllerClient().getBoard();
				repaint();
			}
			view.getControllerClient().changeCountInButton(view);
		}
	}

	
}