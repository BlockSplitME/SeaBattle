package src.view;

import src.TCP.client.ControllerClient;
import src.view.panels.*;

import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;



public class WinMenu extends JFrame {
	
	private ControllerClient controllerClient;
	
	public WinMenu(String winner,ControllerClient controllerClient){
		super("End");
		this.controllerClient = controllerClient;

		JLabel label = new JLabel(winner);
		label.setBounds(100,65,120,20);
		this.add(label);

		this.add(getExit());
		ControllerClient.createFrame(this,300,300,true);
	}

	public JButton getExit() {
		JButton exit = new JButton("Exit");
		exit.setBounds(60,140,180,20);
		exit.addActionListener(new QuitActListener(this));
		return exit;
	}
	public class QuitActListener implements ActionListener {
		JFrame forClosing;
		QuitActListener(JFrame e){ this.forClosing = e; }
		@Override
		public void actionPerformed(ActionEvent e){
			this.forClosing.dispose();
			try {
				controllerClient.breakConnect();
			} catch(IOException error) {
				System.out.println("Server closed");
			}
		}
	}
	
}