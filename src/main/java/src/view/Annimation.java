package src.view;

import src.model.Defolt;
import src.model.Board;
import src.model.ModelBoard;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Annimation extends Thread {
			private int x;
			private int y;
			private Board myBoard;
			private ViewBattle playerView;;
			public Annimation(int x, int y, Board myBoard, ViewBattle playerView) {
				this.x = x;
				this.y = y;
				this.myBoard = myBoard;
				this.playerView = playerView;
			}
			@Override
			public void run(){
				final int animationTime = 4000;//время анимации
            	final long begin = System.currentTimeMillis();
            	int delay = 150;
            	final Timer timer = new Timer(delay, null);
            	timer.addActionListener(new ActionListener() {
                	@Override
                	public void actionPerformed(ActionEvent e) {
                    	long now = System.currentTimeMillis();
                    	long difference = now - begin;
						if(difference <= 400){
							ModelBoard.getBox(myBoard.getMyField(),x,y).setPicture(Defolt.BANG11);
						}
						else if(difference <= 800 && difference > 400){
							ModelBoard.getBox(myBoard.getMyField(),x,y).setPicture(Defolt.BANG21);

						}
						else if(difference <= 1600 && difference > 800){
							ModelBoard.getBox(myBoard.getMyField(),x,y).setPicture(Defolt.BANG31);

						}
						else if(difference < animationTime && difference > 1600){
							if(ModelBoard.getBox(myBoard.getMyField(),x,y).getPicture() == Defolt.BANG41){
								ModelBoard.getBox(myBoard.getMyField(),x,y).setPicture(Defolt.BANG42);
							}
							else{ModelBoard.getBox(myBoard.getMyField(),x,y).setPicture(Defolt.BANG41); }
						}
                		else if(difference >= animationTime) {
							ModelBoard.getBox(myBoard.getMyField(),x,y).setPicture(Defolt.BANG51);                			
                			timer.stop();
                		}
                		playerView.repaintEnemy();
                		playerView.repaintMy();
            		}
            	});
            	timer.start();
			}
}