package src.view;

import src.view.panels.*;
import src.TCP.client.ControllerClient;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JFrame{

	public MainMenu(String title){
		super(title);
		
		this.add(getVsPlayerGame(this));
		this.add(getQuit(this));
		
		ControllerClient.createFrame(this,300,300,true);
	}
//кнопка pvp
	public JButton getVsPlayerGame(MainMenu view){
		JButton pvp = new JButton("with player");
		pvp.setBounds(90,90,120,20);
		pvp.addActionListener(new VsPlayerGameActListener(view));
		return pvp;

	}
	public static class VsPlayerGameActListener implements ActionListener{
		JFrame forClosing;
		VsPlayerGameActListener(JFrame e){ forClosing = e;}
		@Override
		public void actionPerformed(ActionEvent e){
			new InputRoom("room", this.forClosing);
		}
	}
//кнопка выхода
	public JButton getQuit(MainMenu view){
		JButton quit = new JButton("Quit");
		quit.setBounds(90,140,120,20);
		quit.addActionListener(new QuitActListener());
		return quit;
	}
	public static class QuitActListener implements ActionListener {
		QuitActListener(){}
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
}