package src.view.panels;

import src.TCP.client.ControllerClient;
import src.view.*;
import src.model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EnemyField extends JPanel{
	private ViewBattle view;
	private ActionMouse click;
	
	public EnemyField(ViewBattle view){
		this.view = view;
		this.setBounds(40,40,400,400);
		this.setBackground(Color.blue);
		click = new ActionMouse();
		this.addClick();
		
	}
	public void removeClick(){
		this.removeMouseListener(click);
	}
	public void addClick(){
		this.addMouseListener(click);
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		BoxPoint[][] matrix = view.getControllerClient().getEnemyBoard().getMyField();
		for(int i = 0; i < Defolt.COLUMNS; i++){
			for(int j = 0; j < Defolt.ROWS; j++){
				BoxPoint box = matrix[i][j];
				if(box.getPicture() == Defolt.EMPTY || box.getPicture() == Defolt.SHIP){
				 	g.drawImage(ControllerClient.getImage(Defolt.HIDE.name()),box.getX(),box.getY(),this);
				} else{
					g.drawImage(ControllerClient.getImage(box.getPicture().name()),box.getX(),box.getY(),this);
				}
			}
		}
	}
	public class ActionMouse extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e){
			int x = (e.getX()/ Defolt.IMAGE_SIZE);
			int y = (e.getY()/ Defolt.IMAGE_SIZE);

			if(e.getButton() == MouseEvent.BUTTON1){
				System.out.println("Pick:" + x +" "+y);
				view.getControllerClient().doMove(x,y);
			}
		}
	}
}